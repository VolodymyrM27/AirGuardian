package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import systems.ajax.motrechko.dronewarehouse.enums.EmergencyEventType
import java.time.LocalDateTime

data class EmergencyEvent(
    @Id
    val id: ObjectId = ObjectId(),
    val eventType: EmergencyEventType,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String
)
