package systems.ajax.motrechko.airguardian.repository

import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone

interface DroneCustomRepository {
    fun updateDroneInfo(drone: Drone)

    fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): List<Drone>
}
