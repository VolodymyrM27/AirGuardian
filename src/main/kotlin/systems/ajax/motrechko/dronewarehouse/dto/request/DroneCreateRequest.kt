package systems.ajax.motrechko.dronewarehouse.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import systems.ajax.motrechko.dronewarehouse.enums.DroneType
import systems.ajax.motrechko.dronewarehouse.model.Drone


data class DroneCreateRequest @JsonCreator constructor(
    val model: String,
    val type: List<DroneType> = emptyList(),
    val speed: Double,
    val weight: Double,
    val numberOfPropellers: Int,
    val loadCapacity: Double,
    val cost: Double
)

fun DroneCreateRequest.toEntity() = Drone(
    model = model,
    type = type,
    speed = speed,
    weight = weight,
    numberOfPropellers = numberOfPropellers,
    loadCapacity = loadCapacity,
    cost = cost
)
