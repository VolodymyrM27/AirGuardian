package systems.ajax.motrechko.airguardian.drone.domain

import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import java.time.LocalDateTime

data class Drone(
    val id: String? = "",
    val model: String,
    val type: List<DroneType>,
    val speed: Double,
    val weight: Double,
    val numberOfPropellers: Int,
    val loadCapacity: Double,
    val cost: Double,
    var status: DroneStatus,
    var batteryLevel: Double,
    var flightHistory: List<FlightRecord> = emptyList(),
    val size: DroneSize,
    val maxFlightAltitude: Double
) {
    data class FlightRecord(
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
        val startLocation: Coordinates,
        val endLocation: Coordinates,
        val flightDistance: Double,
        val flightDurationPerSeconds: Long
    )
}
