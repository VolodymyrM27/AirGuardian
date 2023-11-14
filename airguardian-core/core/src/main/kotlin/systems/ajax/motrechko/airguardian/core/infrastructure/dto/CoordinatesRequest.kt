package systems.ajax.motrechko.airguardian.core.infrastructure.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.core.shared.util.CoordinatesUtils
import systems.ajax.motrechko.airguardian.core.shared.util.CoordinatesUtils.MAX_LATITUDE

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
