package systems.ajax.motrechko.airguardian.repository

import com.mongodb.client.result.UpdateResult
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone

@Repository
class DeliveryOrderMongoReactiveRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : GenericRepository<DeliveryOrder>(reactiveMongoTemplate, DeliveryOrder::class.java),DeliveryOrderReactiveRepository {
    override fun findAllAvailableDronesToDelivery(totalWeight: Double):Flux<Drone> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(DroneStatus.ACTIVE))
            .addCriteria(Criteria.where("loadCapacity").gte(totalWeight))
            .with(Sort.by(Sort.Direction.ASC, "loadCapacity"))

        return reactiveMongoTemplate.find(query, Drone::class.java)
    }

    override fun findOrderByStatus(status: DeliveryStatus): Flux<DeliveryOrder> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(status.name))
        return reactiveMongoTemplate.find(query, DeliveryOrder::class.java)
    }

    fun deleteByID(id: String): Mono<UpdateResult> {
        val query = Query()
            .addCriteria(Criteria.where("_id").`is`(id))
        val update = Update()
            .set("status", DeliveryStatus.DELETED)
        return reactiveMongoTemplate.updateFirst(query, update, DeliveryOrder::class.java)
    }

    override fun findOrdersByDroneId(droneID: String): Flux<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(droneID))
        return reactiveMongoTemplate.find(query, DeliveryOrder::class.java)
    }

    override fun findOrdersByUserId(userID: String): Flux<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(userID))
        return reactiveMongoTemplate.find(query, DeliveryOrder::class.java)
    }
}
