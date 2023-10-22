package systems.ajax.motrechko.airguardian.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.enums.EmergencyEventType
import java.time.LocalDateTime

@Document("emergency_event")
data class EmergencyEvent(
    @Id
    val id: ObjectId = ObjectId(),
    val eventType: EmergencyEventType = EmergencyEventType.OTHER,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String,
    val emergencyEventStatus: EmergencyEventStatus = EmergencyEventStatus.NEW,
    val droneId: ObjectId? = null
)
