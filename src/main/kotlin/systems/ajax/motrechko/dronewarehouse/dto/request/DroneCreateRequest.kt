package systems.ajax.motrechko.dronewarehouse.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import systems.ajax.motrechko.dronewarehouse.enums.DroneType
import systems.ajax.motrechko.dronewarehouse.model.Drone
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming;

data class DroneCreateRequest @JsonCreator constructor(
    val model: String,
    val type: List<DroneType> = emptyList(),
    val speed: Double,
    val weight: Double,
    @param:JsonProperty("number_of_propellers")
    @get:JsonProperty("number_of_propellers")
    val numberOfPropellers: Int,
    @param:JsonProperty("load_capacity")
    @get:JsonProperty("load_capacity")
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
