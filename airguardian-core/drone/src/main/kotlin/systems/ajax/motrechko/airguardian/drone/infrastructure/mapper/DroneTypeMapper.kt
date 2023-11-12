package systems.ajax.motrechko.airguardian.drone.infrastructure.mapper

import systems.ajax.motrechko.airguardian.drone.domain.DroneType
import systems.ajax.motrechko.airguardian.commonresponse.drone.DroneType as ProtoDroneType


fun DroneType.toProtoDroneType(droneType: DroneType): ProtoDroneType {
    return when (droneType) {
        DroneType.FPV -> ProtoDroneType.FPV
        DroneType.SCOUT -> ProtoDroneType.SCOUT
        DroneType.BOMBER -> ProtoDroneType.BOMBER
        DroneType.MONITORING -> ProtoDroneType.MONITORING
        DroneType.DELIVERY -> ProtoDroneType.DELIVERY
    }
}
