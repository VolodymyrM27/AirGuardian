package systems.ajax.motrechko.dronewarehouse.dto.response

import org.bson.types.ObjectId
import systems.ajax.motrechko.dronewarehouse.enums.DroneType
import systems.ajax.motrechko.dronewarehouse.model.Drone

data class DroneResponse(
    val id: String,
    val model: String,
    val type: List<DroneType> = emptyList(),
    val speed: Double,
    val weight: Double,
    val numberOfPropellers: Int,
    val loadCapacity: Double,
    val cost: Double
)

fun Drone.toResponse() = DroneResponse(
    id = id.toHexString(),
    model = model,
    type = type,
    speed = speed,
    weight = weight,
    numberOfPropellers = numberOfPropellers,
    loadCapacity = loadCapacity,
    cost = cost
)
fun List<Drone>.toResponse(): List<DroneResponse> {
    return this.map { drone ->
        DroneResponse(
            id = drone.id.toHexString(),
            model = drone.model,
            type = drone.type,
            speed = drone.speed,
            weight = drone.weight,
            numberOfPropellers = drone.numberOfPropellers,
            loadCapacity = drone.loadCapacity,
            cost = drone.cost
        )
    }
}
