package systems.ajax.motrechko.airguardian.dto.response

import systems.ajax.motrechko.airguardian.enums.DroneSize
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.commonresponse.drone.Drone as ProtoDrone
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneType as ProtoDroneType
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneSize as ProtoDroneSize
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneStatus as ProtoDroneStatus

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
    type = type.toList(),
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

fun DroneResponse.toProtoDrone(): ProtoDrone {
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


//TODO remove this from here to another file
fun DroneType.toProtoDroneType(droneType: DroneType): ProtoDroneType {
    return when (droneType) {
        DroneType.FPV -> ProtoDroneType.FPV
        DroneType.SCOUT -> ProtoDroneType.SCOUT
        DroneType.BOMBER -> ProtoDroneType.BOMBER
        DroneType.MONITORING -> ProtoDroneType.MONITORING
        DroneType.DELIVERY -> ProtoDroneType.DELIVERY
    }
}

fun DroneSize.toProtoDroneSize(): ProtoDroneSize {
    return when (this) {
        DroneSize.SMALL -> ProtoDroneSize.SMALL
        DroneSize.MEDIUM -> ProtoDroneSize.MEDIUM
        DroneSize.LARGE -> ProtoDroneSize.LARGE
    }
}

fun DroneStatus.toProtoDroneStatus(): ProtoDroneStatus {
    return when (this) {
        DroneStatus.INACTIVE -> ProtoDroneStatus.INACTIVE
        DroneStatus.ACTIVE -> ProtoDroneStatus.ACTIVE
        DroneStatus.CHARGING -> ProtoDroneStatus.CHARGING
        DroneStatus.IN_SELECTION -> ProtoDroneStatus.IN_SELECTION
        DroneStatus.BUSY -> ProtoDroneStatus.BUSY
        DroneStatus.NEED_TO_CHARGE -> ProtoDroneStatus.NEED_TO_CHARGE
    }
}
