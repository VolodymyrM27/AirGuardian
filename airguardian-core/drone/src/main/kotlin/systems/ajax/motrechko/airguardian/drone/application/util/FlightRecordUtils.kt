package systems.ajax.motrechko.airguardian.drone.application.util

import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.core.shared.util.CoordinatesUtils
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import java.time.LocalDateTime

object FlightRecordUtils {
    private const val PLUG_DELIVERY_TIME_MINUTES: Long = 30
    private const val SECONDS_PER_MINUTE: Long = 60
    fun createFlightRecord(
        startTime: LocalDateTime,
        startLocation: Coordinates,
        endLocation: Coordinates
    ): Drone.FlightRecord {
        val endTime = startTime.plusMinutes(PLUG_DELIVERY_TIME_MINUTES)
        val flightDistance = CoordinatesUtils.calculateFlightDistance(startLocation, endLocation)

        return Drone.FlightRecord(
            startTime = startTime,
            endTime = endTime,
            startLocation = startLocation,
            endLocation = endLocation,
            flightDistance = flightDistance,
            flightDurationPerSeconds = PLUG_DELIVERY_TIME_MINUTES * SECONDS_PER_MINUTE
        )
    }
}
