package systems.ajax.motrechko.airguardian.dto.response

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.enums.EmergencyEventType
import systems.ajax.motrechko.airguardian.model.Coordinates
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







