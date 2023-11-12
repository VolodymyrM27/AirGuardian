package systems.ajax.motrechko.airguardian.emergencyevent.domain

import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import java.time.LocalDateTime

data class EmergencyEvent(
    val id: String? = "",
    val eventType: EmergencyEventType = EmergencyEventType.OTHER,
    val location: Coordinates,
    val timestamp: LocalDateTime,
    val description: String,
    val emergencyEventStatus: EmergencyEventStatus = EmergencyEventStatus.NEW,
    val droneId: String?,
)
