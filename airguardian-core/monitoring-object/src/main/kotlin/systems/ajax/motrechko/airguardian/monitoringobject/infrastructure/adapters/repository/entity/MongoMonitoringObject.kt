package systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.repository.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObjectType

@Document("monitoring_object")
data class MongoMonitoringObject(
    @Id
    val id: ObjectId?,
    val name: String = "",
    val objectType: MonitoringObjectType,
    val coordinates: Coordinates
)
