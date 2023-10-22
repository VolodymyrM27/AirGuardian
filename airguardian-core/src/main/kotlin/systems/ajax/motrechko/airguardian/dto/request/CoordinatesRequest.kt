package systems.ajax.motrechko.airguardian.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.utils.CoordinatesUtils
import systems.ajax.motrechko.airguardian.utils.CoordinatesUtils.MAX_LATITUDE
data class CoordinatesRequest(
    @field:Min(
        value = CoordinatesUtils.MIN_LATITUDE.toLong(),
        message = "Latitude must be at least ${CoordinatesUtils.MIN_LATITUDE}"
    )
    @field:Max(
        value = MAX_LATITUDE.toLong(),
        message = "Latitude must be at most ${CoordinatesUtils.MIN_LATITUDE}"
    )
    val latitude: Double,
    @field:Min(
        value = CoordinatesUtils.MIN_LONGITUDE.toLong(),
        message = "Longitude must be at least ${CoordinatesUtils.MIN_LONGITUDE}"
    )
    @field:Max(
        value = CoordinatesUtils.MAX_LONGITUDE.toLong(),
        message = "Longitude must be at most ${CoordinatesUtils.MAX_LONGITUDE}"
    )
    val longitude: Double
)

fun CoordinatesRequest.toEntity() = Coordinates(
    latitude = latitude,
    longitude = longitude
)
