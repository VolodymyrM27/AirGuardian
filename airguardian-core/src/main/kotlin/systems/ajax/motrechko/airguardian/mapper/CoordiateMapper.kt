package systems.ajax.motrechko.airguardian.mapper

import com.google.type.LatLng as ProtoLatLng
import systems.ajax.motrechko.airguardian.model.Coordinates

fun Coordinates.toProtoCoordinates(): ProtoLatLng {
    return ProtoLatLng.newBuilder()
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .build()
}

fun ProtoLatLng.toCoordinates(): Coordinates {
    return Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )
}
