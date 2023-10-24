package systems.ajax.motrechko.airguardian.mapper

import systems.ajax.motrechko.airguardian.model.Coordinates

fun Coordinates.toProtoCoordinates(): systems.ajax.motrechko.airguardian.commonresponse.event.Coordinates {
    return systems.ajax.motrechko.airguardian.commonresponse.event.Coordinates.newBuilder()
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .build()
}

fun systems.ajax.motrechko.airguardian.commonresponse.event.Coordinates.toCoordinates(): Coordinates {
    return Coordinates(
        latitude = this.latitude,
        longitude = this.longitude
    )
}
