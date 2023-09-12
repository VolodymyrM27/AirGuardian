package systems.ajax.motrechko.airguardian.model

import kotlin.random.Random

data class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        private const val MIN_LATITUDE = -90.0
        private const val MAX_LATITUDE = 90.0
        private const val MIN_LONGITUDE = -180.0
        private const val MAX_LONGITUDE = 180.0
    }

    fun generateRandomCoordinates(): Coordinates {
        val randomLatitude = Random.nextDouble(MIN_LATITUDE, MAX_LATITUDE)
        val randomLongitude = Random.nextDouble(MIN_LONGITUDE, MAX_LONGITUDE)

        return Coordinates(randomLatitude, randomLongitude)
    }
}

