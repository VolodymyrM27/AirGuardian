package systems.ajax.motrechko.airguardian.controller.nats

import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.drone.Drone as ProtoDrone
import systems.ajax.motrechko.airguardian.dto.response.toProtoDrone
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject.EmergencyRequest.GET_ALL
import systems.ajax.motrechko.airguardian.service.DroneService

@Component
class GetAllDronesNatsController(
    private  val droneService: DroneService
): NatsController<GetAllDronesRequest, GetAllDronesResponse> {
    override val subject: String = GET_ALL
    override val parser: Parser<GetAllDronesRequest> = GetAllDronesRequest.parser()

    override fun handle(request: GetAllDronesRequest): Mono<GetAllDronesResponse> {
        return droneService.getAllDrones()
            .map { drones -> buildSuccessResponse(drones.map { it.toProtoDrone() }) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(deviceList: List<ProtoDrone>): GetAllDronesResponse =
        GetAllDronesResponse.
        newBuilder().apply {
            successBuilder.addAllDrones(deviceList)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): GetAllDronesResponse =
        GetAllDronesResponse.newBuilder().apply {
            failureBuilder.setMessage("Devices find failed by $exception: $message")
        }.build()
}
