package systems.ajax.motrechko.airguardian.drone.infrastructure.dto.response

import systems.ajax.motrechko.airguardian.drone.domain.DroneSize
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.domain.DroneType

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
