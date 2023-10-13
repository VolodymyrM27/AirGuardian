package systems.ajax.motrechko.airguardian.repository

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.UpdateResult
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone

@Repository
class DroneRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GenericRepository<Drone>(reactiveMongoTemplate, Drone::class.java), DroneCustomReactiveRepository {
    override fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): Flux<Drone> {
        val criteria = Criteria.where("batteryLevel").lte(batteryLevel)
            .and("status").`in`(statusesList)
        val query = Query(criteria)
        return this.reactiveMongoTemplate.find(query, Drone::class.java)
    }

    override fun findAllByStatus(droneStatus: DroneStatus): Flux<Drone> {
        val criteria =  Query().addCriteria(Criteria.where("status").`is`(droneStatus))
        return reactiveMongoTemplate.find(criteria, Drone::class.java)
    }

    override fun updateManyDronesStatus(dronesIds: List<String>, newStatus: DroneStatus): Mono<BulkWriteResult> {
        val bulkOperations = reactiveMongoTemplate
            .bulkOps(BulkOperations.BulkMode.ORDERED, Drone::class.java)

        dronesIds.forEach { droneId ->
            val query = Query(Criteria.where("_id").`is`(droneId))
            val update = Update().apply {
                set("status", newStatus)
            }
            bulkOperations.updateOne(query, update)
        }

        return bulkOperations.execute()
    }

    override fun updateDroneStatus(droneId: String, newStatus: DroneStatus): Mono<UpdateResult> {
        val query = Query(Criteria.where("_id").`is`(droneId))
        val update = Update().apply {
            set("status", newStatus)
        }
        return reactiveMongoTemplate.updateFirst(query, update, Drone::class.java )
    }
}
