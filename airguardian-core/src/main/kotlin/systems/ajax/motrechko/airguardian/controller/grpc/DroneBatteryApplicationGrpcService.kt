package systems.ajax.motrechko.airguardian.controller.grpc

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.ReactorChargingDroneBatteryApplicationGrpc
import systems.ajax.motrechko.airguardian.controller.nats.BatteryDroneChargingApplicationEventNatsController
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationResponse
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEventList
import systems.ajax.motrechko.airguardian.service.DroneBatteryService

class DroneBatteryApplicationGrpcService(
    private val droneBatteryService: DroneBatteryService,
    private val batteryDroneChargingApplicationEventNatsController: BatteryDroneChargingApplicationEventNatsController<DroneBatteryChargingApplicationEventList>
): ReactorChargingDroneBatteryApplicationGrpc.ChargingDroneBatteryApplicationImplBase() {
    override fun getBatteryChargingApplication(request: Mono<GetAllApplicationRequest>)
    : Flux<GetAllApplicationResponse> {
      //  return request.flatMapMany { handleBatteryChargingApplication(it) }
        return Flux.empty()
    }

//    private fun handleBatteryChargingApplication(): Flux<GetAllApplicationResponse> {
//        return droneBatteryService.getBatteryApplications()
//            .flatMap{ initState ->
//                batteryDroneChargingApplicationEventNatsController.subscribeToEvent(NatsSubject.BatteryDroneChargingApplication.GET_ALL)
//                    .map { buildSuccessResponse(it) }
//
//            }
//
//
//    }

//    private fun buildSuccessResponse(droneApplicationList: DroneBatteryChargingApplicationEventList) =
//        GetAllApplicationResponse.newBuilder().apply {
//            successBuilder.setApplication(droneApplicationList)
//        }.build()
}
