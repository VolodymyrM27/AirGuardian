package systems.ajax.motrechko.airguardian.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import systems.ajax.motrechko.airguardian.enums.DroneSize
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.model.Drone

data class DroneCreateRequest(
    @field:NotBlank(message = "model must not be blank")
    val model: String,
    val type: List<DroneType> = emptyList(),
    @field:Min(value = 10, message = "Speed must be at least 10 km/h")
    val speed: Double,
    @field:Min(value = 0, message = "Weight must be at least 0")
    val weight: Double,
    @field:Min(value = 0, message = "Number of propellers must be at least 1")
    val numberOfPropellers: Int,
    @field:Min(value = 0, message = "Load Capacity must be at least 0")
    val loadCapacity: Double,
    @field:Min(value = 0, message = "cost must be at least 0")
    val cost: Double,
    val status: DroneStatus,
    @field:Min(value = 0, message = "Battery level must be at least 0")
    val batteryLevel: Double,
    val size: DroneSize,
    @field:Min(value = 0, message = "Max flight altitude must be at least 0")
    val maxFlightAltitude: Double
)

fun DroneCreateRequest.toEntity() = Drone(
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
