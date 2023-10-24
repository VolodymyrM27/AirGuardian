package systems.ajax.motrechko.airguardian.dto.response

import com.google.protobuf.Timestamp
import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.enums.EmergencyEventType
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.EmergencyEvent
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import systems.ajax.motrechko.airguardian.commonresponse.event.Coordinates as CoordinatesProto
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEvent as EmergencyEventProto
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus as EmergencyEventStatusProto
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType as EmergencyEventTypeProto

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
    id = id.let { it!! },
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
            id = emergencyEvent.id.let { it!! },
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
fun EmergencyEventType.toProtoEmergencyEventType(): EmergencyEventTypeProto {
    return when (this) {
        EmergencyEventType.FIRE -> EmergencyEventTypeProto.FIRE
        EmergencyEventType.ROBBERY -> EmergencyEventTypeProto.ROBBERY
        EmergencyEventType.SHOOTING -> EmergencyEventTypeProto.SHOOTING
        EmergencyEventType.OTHER -> EmergencyEventTypeProto.OTHER
    }
}

fun EmergencyEventTypeProto.toEmergencyEventType(): EmergencyEventType {
    return when (this) {
        EmergencyEventTypeProto.FIRE -> EmergencyEventType.FIRE
        EmergencyEventTypeProto.ROBBERY -> EmergencyEventType.ROBBERY
        EmergencyEventTypeProto.SHOOTING -> EmergencyEventType.SHOOTING
        EmergencyEventTypeProto.OTHER -> EmergencyEventType.OTHER
        else -> EmergencyEventType.OTHER
    }
}

fun Coordinates.toProtoCoordinates(): CoordinatesProto {
    return CoordinatesProto.newBuilder()
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .build()
}

fun CoordinatesProto.toCoordinates(): Coordinates {
    return Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun LocalDateTime.toProtoTimestampBuilder(): Timestamp.Builder {
    val instant = this.atZone(ZoneOffset.UTC).toInstant()
    return Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochSecond(seconds, nanos.toLong())
    return instant.atZone(ZoneOffset.UTC).toLocalDateTime()
}


fun EmergencyEventStatus.toProtoEmergencyEventStatus(): EmergencyEventStatusProto {
    return when (this) {
        EmergencyEventStatus.NEW -> EmergencyEventStatusProto.NEW
        EmergencyEventStatus.PROCESSING -> EmergencyEventStatusProto.PROCESSING
        EmergencyEventStatus.SEARCH_DRONE -> EmergencyEventStatusProto.SEARCH_DRONE
        EmergencyEventStatus.DRONE_ON_THE_WAY -> EmergencyEventStatusProto.DRONE_ON_THE_WAY
        EmergencyEventStatus.UNDER_SUPERVISION -> EmergencyEventStatusProto.UNDER_SUPERVISION
        EmergencyEventStatus.FAKE -> EmergencyEventStatusProto.FAKE
        EmergencyEventStatus.FINISHED -> EmergencyEventStatusProto.FINISHED
    }
}

fun EmergencyEventStatusProto.toEmergencyEventStatus(): EmergencyEventStatus {
    return when (this) {
        EmergencyEventStatusProto.NEW -> EmergencyEventStatus.NEW
        EmergencyEventStatusProto.PROCESSING -> EmergencyEventStatus.PROCESSING
        EmergencyEventStatusProto.SEARCH_DRONE -> EmergencyEventStatus.SEARCH_DRONE
        EmergencyEventStatusProto.DRONE_ON_THE_WAY -> EmergencyEventStatus.DRONE_ON_THE_WAY
        EmergencyEventStatusProto.UNDER_SUPERVISION -> EmergencyEventStatus.UNDER_SUPERVISION
        EmergencyEventStatusProto.FAKE -> EmergencyEventStatus.FAKE
        EmergencyEventStatusProto.FINISHED -> EmergencyEventStatus.FINISHED
        else -> EmergencyEventStatus.NEW
    }
}

fun EmergencyEventProto.toEntity(): EmergencyEvent {
    return EmergencyEvent(
        id = if (this.hasId()) {
            ObjectId(this.id)
        } else {
            null
        },
        eventType = this.eventType.toEmergencyEventType(),
        location = this.location.toCoordinates(),
        timestamp = this.timestamp.toLocalDateTime(),
        description = this.description,
        emergencyEventStatus = this.eventStatus.toEmergencyEventStatus(),
        droneId = this.droneId?.let { null }
    )
}
