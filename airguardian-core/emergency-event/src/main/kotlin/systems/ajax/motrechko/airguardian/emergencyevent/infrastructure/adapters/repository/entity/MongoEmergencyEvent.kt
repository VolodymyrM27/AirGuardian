package systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.adapters.repository.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventType
import java.time.LocalDateTime

@Document("emergency_event")
data class MongoEmergencyEvent(
    @Id
    val id: ObjectId? = ObjectId(),
    val eventType: EmergencyEventType = EmergencyEventType.OTHER,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String,
    val emergencyEventStatus: EmergencyEventStatus = EmergencyEventStatus.NEW,
    val droneId: ObjectId? = null
)
