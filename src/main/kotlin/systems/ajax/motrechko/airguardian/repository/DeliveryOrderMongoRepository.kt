package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.enums.DroneStatus
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
}
