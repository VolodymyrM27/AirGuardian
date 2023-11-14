package systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.repository.mongo

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.monitoringobject.application.port.MonitoringObjectRepositoryOutPort
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObject
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.repository.entity.MongoMonitoringObject
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.mapper.toDomain
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.mapper.toMongoMonitoringObject

@Repository
class MonitoringObjectRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : MonitoringObjectRepositoryOutPort {
    override fun findAll() = reactiveMongoTemplate.findAll(MongoMonitoringObject::class.java).map { it.toDomain() }

    override fun findById(id: String) =
        reactiveMongoTemplate.findById(id, MongoMonitoringObject::class.java).map { it.toDomain() }

    override fun deleteById(id: String) =
        reactiveMongoTemplate.remove(Query(where("_id").`is`(id)), MongoMonitoringObject::class.java)
            .map { it.deletedCount > 0 }


    override fun save(monitoringObject: MonitoringObject) =
        reactiveMongoTemplate.save(monitoringObject.toMongoMonitoringObject()).map { it.toDomain() }

    override fun update(monitoringObject: MonitoringObject) = this.save(monitoringObject)
}
