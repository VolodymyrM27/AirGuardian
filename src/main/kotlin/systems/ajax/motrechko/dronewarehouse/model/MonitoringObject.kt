package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import systems.ajax.motrechko.dronewarehouse.enums.MonitoringObjectType

data class MonitoringObject(
    @Id
    val id: ObjectId = ObjectId(),
    val name: String = "",
    val objectType: MonitoringObjectType,
    val coordinates: Coordinates
)
