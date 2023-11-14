package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.repository.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplicationStatus
import java.time.LocalDateTime

@Document("charging_drone_battery_application")
data class MongoBatteryApplication(
    @Id
    val id: ObjectId = ObjectId(),
    val serviceMessage: String = "",
    val droneId: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: BatteryApplicationStatus = BatteryApplicationStatus.NEW,
)
