package systems.ajax.motrechko.airguardian.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.enums.BatteryApplicationStatus
import java.time.LocalDateTime

@Document("charging_drone_battery_application")
data class BatteryApplication(
    @Id
    val id: ObjectId = ObjectId.get(),
    val serviceMessage: String = "",
    val droneId: String = "",
    val timestamp: LocalDateTime,
    val status: BatteryApplicationStatus,
)
