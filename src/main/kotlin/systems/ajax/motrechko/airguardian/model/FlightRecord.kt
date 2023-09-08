package systems.ajax.motrechko.airguardian.model

import java.time.LocalDateTime
import kotlin.time.Duration

data class FlightRecord(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val startLocation: Coordinates,
    val endLocation: Coordinates,
    val flightDistance: Double,
    val flightDuration: Duration
)
