package systems.ajax.motrechko.airguardian.mapper

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType.SHOOTING
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType.FIRE
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType.OTHER
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType.ROBBERY
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.FAKE
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.FINISHED
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.NEW
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.PROCESSING
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.SEARCH_DRONE
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.DRONE_ON_THE_WAY
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus.UNDER_SUPERVISION
import systems.ajax.motrechko.airguardian.dto.response.EmergencyEventResponse
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.enums.EmergencyEventType
import systems.ajax.motrechko.airguardian.model.EmergencyEvent
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEvent as EmergencyEventProto
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus as EmergencyEventStatusProto
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType as EmergencyEventTypeProto

fun EmergencyEventStatus.toProtoEmergencyEventStatus(): EmergencyEventStatusProto {
    return when (this) {
        EmergencyEventStatus.NEW -> NEW
        EmergencyEventStatus.PROCESSING -> PROCESSING
        EmergencyEventStatus.SEARCH_DRONE -> SEARCH_DRONE
        EmergencyEventStatus.DRONE_ON_THE_WAY -> DRONE_ON_THE_WAY
        EmergencyEventStatus.UNDER_SUPERVISION -> UNDER_SUPERVISION
        EmergencyEventStatus.FAKE -> FAKE
        EmergencyEventStatus.FINISHED -> FINISHED
    }
}

fun EmergencyEventStatusProto.toEmergencyEventStatus(): EmergencyEventStatus {
    return when (this) {
        NEW -> EmergencyEventStatus.NEW
        PROCESSING -> EmergencyEventStatus.PROCESSING
        SEARCH_DRONE -> EmergencyEventStatus.SEARCH_DRONE
        DRONE_ON_THE_WAY -> EmergencyEventStatus.DRONE_ON_THE_WAY
        UNDER_SUPERVISION -> EmergencyEventStatus.UNDER_SUPERVISION
        FAKE -> EmergencyEventStatus.FAKE
        FINISHED -> EmergencyEventStatus.FINISHED
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

fun EmergencyEventType.toProtoEmergencyEventType(): EmergencyEventTypeProto {
    return when (this) {
        EmergencyEventType.FIRE -> FIRE
        EmergencyEventType.ROBBERY -> ROBBERY
        EmergencyEventType.SHOOTING -> SHOOTING
        EmergencyEventType.OTHER -> OTHER
    }
}

fun EmergencyEventTypeProto.toEmergencyEventType(): EmergencyEventType {
    return when (this) {
        FIRE -> EmergencyEventType.FIRE
        ROBBERY -> EmergencyEventType.ROBBERY
        SHOOTING -> EmergencyEventType.SHOOTING
        OTHER -> EmergencyEventType.OTHER
        else -> EmergencyEventType.OTHER
    }
}

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
    return systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEvent.newBuilder()
        .setId(id.toHexString())
        .setEventType(this.eventType.toProtoEmergencyEventType())
        .setLocation(this.location.toProtoCoordinates())
        .setTimestamp(timestamp.toProtoTimestampBuilder())
        .setDescription(description)
        .setEventStatus(this.emergencyEventStatus.toProtoEmergencyEventStatus())
        .setDroneId(droneId?.toHexString() ?: "")
        .build()
}
