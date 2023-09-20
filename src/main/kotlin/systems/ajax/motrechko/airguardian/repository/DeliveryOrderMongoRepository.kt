package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone

@Repository
class DeliveryOrderMongoRepository(
    private val mongoTemplate: MongoTemplate,
) : DeliveryOrderCustomRepository {
    override fun findAllAvailableDronesToDelivery(totalWeight: Double): List<Drone> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(DroneStatus.ACTIVE))
            .addCriteria(Criteria.where("loadCapacity").gte(totalWeight))
        return mongoTemplate.find(query, Drone::class.java)
    }

    override fun findOrderByStatus(status: DeliveryStatus): List<DeliveryOrder> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(status.name))
        return mongoTemplate.find(query, DeliveryOrder::class.java)
    }

    override fun deleteByID(id: String) {
        val query = Query()
            .addCriteria(Criteria.where("id").`is`(id))
        val update = Update()
            .set("status", DeliveryStatus.DELETED)
        mongoTemplate.updateFirst(query, update, DeliveryOrder::class.java)
    }

    override fun findOrdersByDroneId(droneID: String): List<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(droneID))
        return mongoTemplate.find(query, DeliveryOrder::class.java)
    }

    override fun findOrdersByUserId(userID: String): List<DeliveryOrder> {
        val query = Query(Criteria.where("deliveryDroneIds").`in`(userID))
        return mongoTemplate.find(query, DeliveryOrder::class.java)
    }

    override fun update(order: DeliveryOrder) {
        val query = Query()
            .addCriteria(Criteria.where("_id").`is`(order.id))
        val update = Update()
            .set("customerName", order.customerName)
            .set("items", order.items)
            .set("status", order.status)
            .set("deliveryAddress", order.deliveryAddress)
            .set("deliveryCoordinates", order.deliveryCoordinates)
            .set("deliveryDroneIds", order.deliveryDroneIds)
        mongoTemplate.updateFirst(query, update, DeliveryOrder::class.java)
    }
}
