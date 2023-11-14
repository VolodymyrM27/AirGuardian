package systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.nats

import com.google.protobuf.Parser
import systems.ajax.motrechko.airguardian.core.infrastructure.adapters.nats.NatsController
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.drone.application.port.DroneServiceInPort
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toProtoDrone
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject.EmergencyRequest.GET_ALL
import systems.ajax.motrechko.airguardian.commonresponse.drone.Drone as ProtoDrone

@Component
class GetAllDronesNatsController(
    private val droneService: DroneServiceInPort
): NatsController<GetAllDronesRequest, GetAllDronesResponse> {
    override val subject: String = GET_ALL
    override val parser: Parser<GetAllDronesRequest> = GetAllDronesRequest.parser()

    override fun handle(request: GetAllDronesRequest): Mono<GetAllDronesResponse> {
        return droneService.getAllDrones()
            .map { drone -> drone.toProtoDrone() }
            .collectList()
            .map { drones -> buildSuccessResponse(drones) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(droneList: List<ProtoDrone>): GetAllDronesResponse =
        GetAllDronesResponse.newBuilder().apply {
            successBuilder.addAllDrones(droneList)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): GetAllDronesResponse =
        GetAllDronesResponse.newBuilder().apply {
            failureBuilder.setMessage("Drone find failed by $exception: $message")
        }.build()
}
