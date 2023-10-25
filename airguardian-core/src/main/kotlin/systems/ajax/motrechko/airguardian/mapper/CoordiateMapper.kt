package systems.ajax.motrechko.airguardian.mapper

import com.google.type.LatLng
import systems.ajax.motrechko.airguardian.model.Coordinates

fun Coordinates.toProtoCoordinates(): LatLng {
    return LatLng.newBuilder()
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .build()
}

fun LatLng.toCoordinates(): Coordinates {
    return Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )
}
