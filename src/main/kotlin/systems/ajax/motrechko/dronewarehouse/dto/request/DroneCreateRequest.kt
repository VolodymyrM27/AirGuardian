package systems.ajax.motrechko.dronewarehouse.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import systems.ajax.motrechko.dronewarehouse.enums.DroneSize
import systems.ajax.motrechko.dronewarehouse.enums.DroneStatus
import systems.ajax.motrechko.dronewarehouse.enums.DroneType
import systems.ajax.motrechko.dronewarehouse.model.Drone


data class DroneCreateRequest @JsonCreator constructor(
    @NotBlank
    val model: String,
    val type: List<DroneType> = emptyList(),
    @NotBlank
    @Min(0)
    val speed: Double,
    @NotBlank
    @Min(0)
    val weight: Double,
    @NotBlank
    @Min(0)
    val numberOfPropellers: Int,
    @NotBlank
    @Min(0)
    val loadCapacity: Double,
    @NotBlank
    @Min(0)
    val cost: Double,
    val status: DroneStatus,
    val batteryLevel: Double,
    val size: DroneSize,
    val maxFlightAltitude: Double
)

fun DroneCreateRequest.toEntity() = Drone(
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
