package systems.ajax.motrechko.airguardian.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.enums.MonitoringObjectType

@Document("monitoring_object")
data class MonitoringObject(
    @Id
    val id: ObjectId? = ObjectId(),
    val name: String = "",
    val objectType: MonitoringObjectType,
    val coordinates: Coordinates
)
