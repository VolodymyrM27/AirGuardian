package systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.dto

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventType
import java.time.LocalDateTime

data class EmergencyEventResponse(
    val id: ObjectId,
    val eventType: EmergencyEventType,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String,
    val emergencyEventStatus: EmergencyEventStatus,
    val droneId: ObjectId? = null
)







