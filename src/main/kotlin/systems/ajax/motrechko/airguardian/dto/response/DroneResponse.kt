package systems.ajax.motrechko.airguardian.dto.response

import systems.ajax.motrechko.airguardian.enums.DroneSize
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.model.Drone

data class DroneResponse(
    val id: String,
    val model: String,
    val type: List<DroneType> = emptyList(),
    val speed: Double,
    val weight: Double,
    val numberOfPropellers: Int,
    val loadCapacity: Double,
    val cost: Double,
    val status: DroneStatus,
    val batteryLevel: Double,
    val size: DroneSize,
    val maxFlightAltitude: Double
)

fun Drone.toResponse() = DroneResponse(
    id = id.toHexString(),
    model = model,
    type = type,
    speed = speed,
    weight = weight,
    numberOfPropellers = numberOfPropellers,
    loadCapacity = loadCapacity,
    cost = cost,
    status = status,
    batteryLevel = batteryLevel,
    size = size,
    maxFlightAltitude = maxFlightAltitude
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
            cost = drone.cost,
            status = drone.status,
            batteryLevel = drone.batteryLevel,
            size = drone.size,
            maxFlightAltitude = drone.maxFlightAltitude
        )
    }
}
