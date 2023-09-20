package systems.ajax.motrechko.airguardian.utils

import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.FlightRecord
import java.time.LocalDateTime

object FlightRecordUtils {
    private const val PLUG_DELIVERY_TIME_MINUTES: Long = 30
    private const val SECONDS_PER_MINUTE: Long = 60
    fun createFlightRecord(
        startTime: LocalDateTime,
        startLocation: Coordinates,
        endLocation: Coordinates
    ): FlightRecord {
        val endTime = startTime.plusMinutes(PLUG_DELIVERY_TIME_MINUTES)
        val flightDistance = CoordinatesUtils.calculateFlightDistance(startLocation, endLocation)

        return FlightRecord(
            startTime = startTime,
            endTime = endTime,
            startLocation = startLocation,
            endLocation = endLocation,
            flightDistance = flightDistance,
            flightDurationPerSeconds = PLUG_DELIVERY_TIME_MINUTES * SECONDS_PER_MINUTE
        )
    }
}
