package systems.ajax.motrechko.airguardian.drone.infrastructure.mapper

import systems.ajax.motrechko.airguardian.drone.domain.DroneSize
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneSize as ProtoDroneSize

fun DroneSize.toProtoDroneSize(): ProtoDroneSize {
    return when (this) {
        DroneSize.SMALL -> ProtoDroneSize.SMALL
        DroneSize.MEDIUM -> ProtoDroneSize.MEDIUM
        DroneSize.LARGE -> ProtoDroneSize.LARGE
    }
}
