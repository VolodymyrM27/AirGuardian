package systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.mongo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DeliveryOrderRepositoryOutPort
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus
import systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.entity.DeliveryOrderMongo
import systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.mapper.toDomain
import systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.mapper.toMongoDeliveryOrder
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toDrone

@Repository
class DeliveryOrderMongoReactiveRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : DeliveryOrderRepositoryOutPort {
    override fun findAllAvailableDronesToDelivery(totalWeight: Double):Flux<Drone> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(DroneStatus.ACTIVE))
            .addCriteria(Criteria.where("loadCapacity").gte(totalWeight))
            .with(Sort.by(Sort.Direction.ASC, "loadCapacity"))

        return reactiveMongoTemplate.find(query, MongoDrone::class.java).map { it.toDrone() }
    }

    override fun findOrderByStatus(status: DeliveryStatus): Flux<DeliveryOrder> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(status.name))
        return reactiveMongoTemplate.find(query, DeliveryOrderMongo::class.java).map { it.toDomain() }
    }

    override fun findOrdersByDroneId(droneID: String): Flux<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(droneID))
        return reactiveMongoTemplate.find(query, DeliveryOrderMongo::class.java).map { it.toDomain() }
    }

    override fun findOrdersByUserId(userID: String): Flux<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(userID))
        return reactiveMongoTemplate.find(query, DeliveryOrderMongo::class.java).map { it.toDomain() }
    }

    override fun save(entity: DeliveryOrder): Mono<DeliveryOrder> =
        reactiveMongoTemplate.save(entity.toMongoDeliveryOrder()).map { it.toDomain() }

    override fun findById(id: String): Mono<DeliveryOrder> =
        reactiveMongoTemplate.findById(id, DeliveryOrderMongo::class.java).map { it.toDomain() }

    override fun findAll(): Flux<DeliveryOrder> =
        reactiveMongoTemplate.findAll(DeliveryOrderMongo::class.java).map { it.toDomain() }

    override fun deleteById(id: String): Mono<Boolean> {
        val query = Query()
            .addCriteria(Criteria.where("_id").`is`(id))
        val update = Update().apply {
            set("status", DeliveryStatus.DELETED)
        }
        return reactiveMongoTemplate.updateFirst(query, update, DeliveryOrderMongo::class.java)
            .handle { result, sink ->
                if(result.modifiedCount > 0) sink.next(true)
                else sink.next(false)
            }
    }
}
