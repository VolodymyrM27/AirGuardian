package systems.ajax.motrechko.airguardian.drone.infrastructure.mapper

import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneStatus as ProtoDroneStatus

fun DroneStatus.toProtoDroneStatus(): ProtoDroneStatus {
    return when (this) {
        DroneStatus.INACTIVE -> ProtoDroneStatus.INACTIVE
        DroneStatus.ACTIVE -> ProtoDroneStatus.ACTIVE
        DroneStatus.CHARGING -> ProtoDroneStatus.CHARGING
        DroneStatus.IN_SELECTION -> ProtoDroneStatus.IN_SELECTION
        DroneStatus.BUSY -> ProtoDroneStatus.BUSY
        DroneStatus.NEED_TO_CHARGE -> ProtoDroneStatus.NEED_TO_CHARGE
    }
}
