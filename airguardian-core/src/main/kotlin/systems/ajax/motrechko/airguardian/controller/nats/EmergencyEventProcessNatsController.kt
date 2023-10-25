package systems.ajax.motrechko.airguardian.controller.nats

import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEvent as ProtoEmergencyEvent
import systems.ajax.motrechko.airguardian.input.reqrepl.event.new_event.proto.EmergencyEventRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.event.new_event.proto.EmergencyEventResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject.EmergencyRequest.NEW_EMERGENCY_EVENT
import systems.ajax.motrechko.airguardian.mapper.toEntity
import systems.ajax.motrechko.airguardian.mapper.toProtoEmergencyEvent
import systems.ajax.motrechko.airguardian.mapper.toResponse
import systems.ajax.motrechko.airguardian.service.EmergencyService
import systems.ajax.motrechko.airguardian.input.reqrepl.event.new_event.proto.EmergencyEventResponse as EmergencyEventResponseProto

@Component
class EmergencyEventProcessNatsController(
    private val emergencyEventService: EmergencyService
) :NatsController<EmergencyEventRequest, EmergencyEventResponse>{
    override val subject: String = NEW_EMERGENCY_EVENT
    override val parser: Parser<EmergencyEventRequest> = EmergencyEventRequest.parser()
    override fun handle(request: EmergencyEventRequest): Mono<EmergencyEventResponse> {
        return emergencyEventService.processEmergencyEvent(request.event.toEntity())
            .map { event -> buildSuccessResponse(event.toResponse().toProtoEmergencyEvent()) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(protoEmergencyEvent: ProtoEmergencyEvent): EmergencyEventResponseProto =
        EmergencyEventResponseProto.newBuilder().apply {
            successBuilder.setEvent(protoEmergencyEvent)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): EmergencyEventResponseProto =
        EmergencyEventResponseProto.newBuilder().apply {
            failureBuilder.setMessage("No available drones were found: $exception: $message")
        }.build()
}
