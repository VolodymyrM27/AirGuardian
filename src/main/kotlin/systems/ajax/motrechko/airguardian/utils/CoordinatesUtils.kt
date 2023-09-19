package systems.ajax.motrechko.airguardian.utils

import systems.ajax.motrechko.airguardian.model.Coordinates
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

object CoordinatesUtils {
    private const val MIN_LATITUDE = -90.0
    private const val MAX_LATITUDE = 90.0
    private const val MIN_LONGITUDE = -180.0
    private const val MAX_LONGITUDE = 180.0
    private const val EARTH_RADIUS = 6371
    private const val DEGREE_OF_LATITUDE_PER_KILOMETER = 111.32

    fun generateRandomCoordinates(): Coordinates {
        val randomLatitude = Random.nextDouble(MIN_LATITUDE, MAX_LATITUDE)
        val randomLongitude = Random.nextDouble(MIN_LONGITUDE, MAX_LONGITUDE)

        return Coordinates(randomLatitude, randomLongitude)
    }

    fun calculateFlightDistance(start: Coordinates, end: Coordinates): Double {
        val startLatitudeRadians = Math.toRadians(start.latitude)
        val endLatitudeRadians = Math.toRadians(end.latitude)

        val deltaLatitudeRadians = Math.toRadians(end.latitude - start.latitude)
        val deltaLongitudeRadians = Math.toRadians(end.longitude - start.longitude)

        val a = sin(deltaLatitudeRadians / 2) * sin(deltaLatitudeRadians / 2) +
                cos(startLatitudeRadians) * cos(endLatitudeRadians) *
                sin(deltaLongitudeRadians / 2) * sin(deltaLongitudeRadians / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS * c
    }

    fun generateRandomCoordinatesWithinRange(
        latitude: Double,
        longitude: Double,
        maxDistance: Double
    ): Coordinates {
        val maxLatitude = latitude + (maxDistance / DEGREE_OF_LATITUDE_PER_KILOMETER)
        val minLatitude = latitude - (maxDistance / DEGREE_OF_LATITUDE_PER_KILOMETER)
        val maxLongitude =
            longitude + (maxDistance / (DEGREE_OF_LATITUDE_PER_KILOMETER * Math.cos(Math.toRadians(latitude))))
        val minLongitude =
            longitude - (maxDistance / (DEGREE_OF_LATITUDE_PER_KILOMETER * Math.cos(Math.toRadians(latitude))))

        val randomLatitude = Random.nextDouble(minLatitude, maxLatitude)
        val randomLongitude = Random.nextDouble(minLongitude, maxLongitude)

        return Coordinates(randomLatitude, randomLongitude)
    }
}
