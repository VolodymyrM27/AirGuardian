package systems.ajax.motrechko.airguardian.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import systems.ajax.motrechko.airguardian.enums.MonitoringObjectType

data class MonitoringObject(
    @Id
    val id: ObjectId = ObjectId(),
    val name: String = "",
    val objectType: MonitoringObjectType,
    val coordinates: Coordinates
)
