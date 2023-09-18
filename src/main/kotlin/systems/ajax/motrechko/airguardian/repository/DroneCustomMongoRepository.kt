package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone

@Repository
class DroneCustomMongoRepository(
    private val mongoTemplate: MongoTemplate,
) : DroneCustomRepository {
    override fun updateDroneInfo(drone: Drone) {
        val query = Query()
            .addCriteria(Criteria.where("_id").`is`(drone.id))
        val update = Update()
            .set("model", drone.model)
            .set("type", drone.type)
            .set("speed", drone.speed)
            .set("weight", drone.weight)
            .set("numberOfPropellers", drone.numberOfPropellers)
            .set("loadCapacity", drone.loadCapacity)
            .set("cost", drone.cost)
            .set("status", drone.status)
            .set("batteryLevel", drone.batteryLevel)
            .set("flightHistory", drone.flightHistory)
            .set("size", drone.size)
            .set("maxFlightAltitude", drone.maxFlightAltitude)

        mongoTemplate.updateFirst(query, update, Drone::class.java)
    }

    override fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): List<Drone> {
        val criteria = Criteria.where("batteryLevel").lte(batteryLevel)
            .and("status").`in`(statusesList)
        val query = Query(criteria)
        return mongoTemplate.find(query, Drone::class.java)
    }
}
