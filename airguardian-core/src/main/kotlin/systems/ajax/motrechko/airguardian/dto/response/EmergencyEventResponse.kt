package systems.ajax.motrechko.airguardian.dto.response

import com.google.protobuf.Timestamp
import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.enums.EmergencyEventType
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.EmergencyEvent
import java.time.LocalDateTime
import java.time.ZoneOffset
import systems.ajax.motrechko.airguardian.input.reqrepl.event.emergency_event.proto.Coordinates as CoordinatesProto
import systems.ajax.motrechko.airguardian.input.reqrepl.event.emergency_event.proto.EmergencyEvent as EmergencyEventProto
import systems.ajax.motrechko.airguardian.input.reqrepl.event.emergency_event.proto.EmergencyEventStatus as EmergencyEventStatusProto
import systems.ajax.motrechko.airguardian.input.reqrepl.event.emergency_event.proto.EmergencyEventType as EmergencyEventTypeProto

data class EmergencyEventResponse(
    val id: ObjectId,
    val eventType: EmergencyEventType,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String,
    val emergencyEventStatus: EmergencyEventStatus,
    val droneId: ObjectId? = null
)

fun EmergencyEvent.toResponse() = EmergencyEventResponse(
        id = id,
        eventType = eventType,
        location = location,
        timestamp = timestamp,
        description = description,
        emergencyEventStatus = emergencyEventStatus,
        droneId = droneId
)

fun List<EmergencyEvent>.toResponse(): List<EmergencyEventResponse> {
    return this.map { emergencyEvent ->
        EmergencyEventResponse(
            id = emergencyEvent.id,
            eventType = emergencyEvent.eventType,
            location = emergencyEvent.location,
            timestamp = emergencyEvent.timestamp,
            description = emergencyEvent.description,
            emergencyEventStatus = emergencyEvent.emergencyEventStatus,
            droneId = emergencyEvent.droneId
        )
    }
}

fun EmergencyEventResponse.toProtoEmergencyEvent(): EmergencyEventProto {
    return EmergencyEventProto.newBuilder()
        .setId(id.toHexString())
        .setEventType(this.eventType.toProtoEmergencyEventType())
        .setLocation(this.location.toProtoCoordinates())
        .setTimestamp(timestamp.toProtoTimestampBuilder())
        .setDescription(description)
        .setEventStatus(this.emergencyEventStatus.toProtoEmergencyEventStatus())
        .setDroneId(droneId?.toHexString() ?: "")
        .build()
}


//TODO all this functions should be in separate file
fun EmergencyEventType.toProtoEmergencyEventType() : EmergencyEventTypeProto {
    return when(this) {
        EmergencyEventType.FIRE -> EmergencyEventTypeProto.FIRE
        EmergencyEventType.ROBBERY -> EmergencyEventTypeProto.ROBBERY
        EmergencyEventType.SHOOTING -> EmergencyEventTypeProto.SHOOTING
        EmergencyEventType.OTHER -> EmergencyEventTypeProto.OTHER
    }
}

fun Coordinates.toProtoCoordinates() : CoordinatesProto {
    return CoordinatesProto.newBuilder()
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .build()
}

fun LocalDateTime.toProtoTimestampBuilder(): Timestamp.Builder {
    val instant = this.atZone(ZoneOffset.UTC).toInstant()
    return Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
}

fun EmergencyEventStatus.toProtoEmergencyEventStatus() : EmergencyEventStatusProto {
    return when(this) {
        EmergencyEventStatus.NEW -> EmergencyEventStatusProto.NEW
        EmergencyEventStatus.PROCESSING -> EmergencyEventStatusProto.PROCESSING
        EmergencyEventStatus.SEARCH_DRONE -> EmergencyEventStatusProto.SEARCH_DRONE
        EmergencyEventStatus.DRONE_ON_THE_WAY -> EmergencyEventStatusProto.DRONE_ON_THE_WAY
        EmergencyEventStatus.UNDER_SUPERVISION -> EmergencyEventStatusProto.UNDER_SUPERVISION
        EmergencyEventStatus.FAKE -> EmergencyEventStatusProto.FAKE
        EmergencyEventStatus.FINISHED -> EmergencyEventStatusProto.FINISHED
    }
}
