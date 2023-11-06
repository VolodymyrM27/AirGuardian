package systems.ajax.motrechko.airguardian.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.ReactorChargingDroneBatteryApplicationGrpc
import systems.ajax.motrechko.airguardian.controller.nats.BatteryDroneChargingApplicationEventNatsController
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.mapper.toDroneBatteryChargingApplicationEventList
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEventList
import systems.ajax.motrechko.airguardian.service.DroneBatteryService

@GrpcService
class DroneBatteryApplicationGrpcService(
    private val droneBatteryService: DroneBatteryService,
    private val batteryDroneChargingApplicationEventNatsController:
        BatteryDroneChargingApplicationEventNatsController<DroneBatteryChargingApplicationEvent>,
): ReactorChargingDroneBatteryApplicationGrpc.ChargingDroneBatteryApplicationImplBase() {
    override fun getBatteryChargingApplication(request: Mono<GetAllApplicationRequest>)
    : Flux<GetAllApplicationResponse> {
        return request.flatMapMany { handleBatteryChargingApplication() }
    }

    private fun handleBatteryChargingApplication(): Flux<GetAllApplicationResponse> {
        return droneBatteryService.getBatteryApplications()
            .collectList()
            .flatMapMany {initialState ->
                batteryDroneChargingApplicationEventNatsController.subscribeToEvent(
                    NatsSubject.BatteryDroneChargingApplication.GET_ALL
                )
                    .map { event -> buildSuccessResponse(event) }
                    .startWith(
                        buildSuccessInitialStateResponse(initialState.toDroneBatteryChargingApplicationEventList())
                    )
            }
    }

    private fun buildSuccessResponse(
        droneApplication: DroneBatteryChargingApplicationEvent
    ): GetAllApplicationResponse =
        GetAllApplicationResponse.newBuilder().apply {
            successBuilder.newStateBuilder.setApplication(droneApplication)
        }.build()

    private fun buildSuccessInitialStateResponse(
        droneApplicationList: DroneBatteryChargingApplicationEventList
    ): GetAllApplicationResponse =
        GetAllApplicationResponse.newBuilder().apply {
            successBuilder.initialStateBuilder.setApplicationList(droneApplicationList)
        }.build()
}
