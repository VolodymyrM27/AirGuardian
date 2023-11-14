package systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.mongo

import com.mongodb.bulk.BulkWriteResult
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toMongoDrone

@Repository
class MongoDroneRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : DroneRepositoryOutPort {
    override fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): Flux<Drone> {
        val criteria = Criteria.where("batteryLevel").lte(batteryLevel)
            .and("status").`in`(statusesList)
        val query = Query(criteria)
        return this.reactiveMongoTemplate.find(query, MongoDrone::class.java).map { it.toDrone() }
    }

    override fun findAllByStatus(droneStatus: DroneStatus): Flux<Drone> {
        val criteria = Query().addCriteria(Criteria.where("status").`is`(droneStatus))
        return reactiveMongoTemplate.find(criteria, MongoDrone::class.java).map { it.toDrone() }
    }

    override fun updateManyDronesStatus(dronesIds: List<String>, newStatus: DroneStatus): Mono<Boolean> {
        val bulkOperations = reactiveMongoTemplate
            .bulkOps(BulkOperations.BulkMode.ORDERED, MongoDrone::class.java)

        dronesIds.forEach { droneId ->
            val query = Query(Criteria.where("_id").`is`(droneId))
            val update = Update().apply {
                set("status", newStatus)
            }
            bulkOperations.updateOne(query, update)
        }

        return bulkOperations.execute().map { bulkResult: BulkWriteResult ->
            bulkResult.modifiedCount == dronesIds.size
        }
    }

    override fun updateDroneStatus(droneId: String, newStatus: DroneStatus): Mono<Boolean> {
        val query = Query(Criteria.where("_id").`is`(droneId))
        val update = Update().apply {
            set("status", newStatus)
        }
        return reactiveMongoTemplate.updateFirst(query, update, MongoDrone::class.java)
            .map { it.modifiedCount > 0 }
    }

    override fun save(entity: Drone): Mono<Drone> =
        reactiveMongoTemplate.save(entity.toMongoDrone()).map { it.toDrone() }

    override fun findById(id: String): Mono<Drone> =
        reactiveMongoTemplate.findById(id, MongoDrone::class.java).map { it.toDrone() }


    override fun findAll(): Flux<Drone> =
        reactiveMongoTemplate.findAll(MongoDrone::class.java).map { it.toDrone() }

    override fun deleteById(id: String): Mono<Boolean> =
        reactiveMongoTemplate.remove(Query(Criteria.where("_id").`is`(id)), MongoDrone::class.java)
            .map { it.deletedCount > 0 }
}
