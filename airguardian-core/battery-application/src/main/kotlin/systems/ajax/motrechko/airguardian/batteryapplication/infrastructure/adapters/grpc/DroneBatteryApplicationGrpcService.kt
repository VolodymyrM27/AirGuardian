package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.grpc

import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.ReactorChargingDroneBatteryApplicationGrpc
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.DroneBatteryServiceInPort
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.mapper.toProtoList
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.nats.subscriber.BatteryApplicationNatsSubscriber
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationsRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationsResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@GrpcService
class DroneBatteryApplicationGrpcService(
    private val droneBatteryService: DroneBatteryServiceInPort,
    private val batteryDroneChargingApplicationEventNatsController:
    BatteryApplicationNatsSubscriber<DroneBatteryChargingApplicationEvent>,
) : ReactorChargingDroneBatteryApplicationGrpc.ChargingDroneBatteryApplicationImplBase() {
    
    override fun getBatteryChargingApplication(request: Mono<GetAllApplicationsRequest>)
            : Flux<GetAllApplicationsResponse> {
        return request.flatMapMany { handleBatteryChargingApplication() }
    }

    private fun handleBatteryChargingApplication(): Flux<GetAllApplicationsResponse> {
        return droneBatteryService.getBatteryApplications()
            .collectList()
            .flatMapMany { initialState ->
                batteryDroneChargingApplicationEventNatsController.subscribeToEvents(
                    NatsSubject.BatteryDroneChargingApplication.NEW_APPLICATION
                )
                    .map { event -> buildSuccessResponse(event.application) }
                    .startWith(
                        buildSuccessInitialStateResponse(initialState.toProtoList())
                    )
            }
    }

    private fun buildSuccessResponse(
        droneApplication: DroneBatteryChargingApplication
    ): GetAllApplicationsResponse =
        GetAllApplicationsResponse.newBuilder().apply {
            successBuilder.newStateBuilder.setApplication(droneApplication)
        }.build()

    private fun buildSuccessInitialStateResponse(
        droneApplicationList: List<DroneBatteryChargingApplication>
    ): GetAllApplicationsResponse = GetAllApplicationsResponse.newBuilder().apply {
        successBuilder.initialStateBuilder.addAllApplicationList(droneApplicationList)
    }.build()
}
