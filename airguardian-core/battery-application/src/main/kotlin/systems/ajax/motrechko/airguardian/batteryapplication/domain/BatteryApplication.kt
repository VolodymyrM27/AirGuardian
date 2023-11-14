package systems.ajax.motrechko.airguardian.batteryapplication.domain

import java.time.LocalDateTime

data class BatteryApplication(
    val id: String? = "",
    val serviceMessage: String = "",
    val droneId: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: BatteryApplicationStatus = BatteryApplicationStatus.NEW,
)
