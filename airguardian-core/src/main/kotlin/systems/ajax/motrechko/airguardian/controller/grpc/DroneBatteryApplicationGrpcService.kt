package systems.ajax.motrechko.airguardian.controller.grpc

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.ReactorChargingDroneBatteryApplicationGrpc
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.application.get_charge_application.GetAllApplicationResponse

class DroneBatteryApplicationGrpcService(

): ReactorChargingDroneBatteryApplicationGrpc.ChargingDroneBatteryApplicationImplBase() {
    override fun getBatteryChargingApplication(request: Mono<GetAllApplicationRequest>?): Flux<GetAllApplicationResponse> {
        return super.getBatteryChargingApplication(request)
    }
}
