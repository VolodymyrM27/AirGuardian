package systems.ajax.motrechko.airguardian.drone.infrastructure.mapper

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.dto.response.DroneResponse
import systems.ajax.motrechko.airguardian.commonresponse.drone.Drone as ProtoDrone

fun Drone.toResponse() = DroneResponse(
    id = id?: "",
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

fun Drone.toMongoDrone() = MongoDrone(
    if (!id.isNullOrEmpty()) ObjectId(id) else ObjectId(),
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
    maxFlightAltitude = maxFlightAltitude,
    flightHistory = flightHistory
)

fun MongoDrone.toDrone() = Drone(
    id = id?.toHexString(),
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
    maxFlightAltitude = maxFlightAltitude,
    flightHistory = flightHistory
)

fun List<Drone>.toResponse(): List<DroneResponse> {
    return this.map { drone ->
        DroneResponse(
            id = drone.id?: "",
            model = drone.model,
            type = drone.type.toList(),
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

fun Drone.toProtoDrone(): ProtoDrone {
    return ProtoDrone.newBuilder()
        .setId(this.id)
        .setModel(this.model)
        .addAllType(this.type.map { it.toProtoDroneType(it) })
        .setSpeed(this.speed)
        .setWeight(this.weight)
        .setNumberOfPropellers(this.numberOfPropellers)
        .setLoadCapacity(this.loadCapacity)
        .setCost(this.cost)
        .setStatus(this.status.toProtoDroneStatus())
        .setBatteryLevel(this.batteryLevel)
        .setSize(this.size.toProtoDroneSize())
        .setMaxFlightAltitude(this.maxFlightAltitude)
        .build()
}
