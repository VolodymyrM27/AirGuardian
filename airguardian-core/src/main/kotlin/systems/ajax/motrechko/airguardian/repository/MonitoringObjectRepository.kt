package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.model.MonitoringObject

@Repository
class MonitoringObjectRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): GenericRepository<MonitoringObject>(reactiveMongoTemplate, MonitoringObject::class.java)
