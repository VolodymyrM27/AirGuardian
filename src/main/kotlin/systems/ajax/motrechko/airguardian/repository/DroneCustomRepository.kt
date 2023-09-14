package systems.ajax.motrechko.airguardian.repository

import systems.ajax.motrechko.airguardian.model.Drone

interface DroneCustomRepository {
    fun updateDroneInfo(drone: Drone)
}
