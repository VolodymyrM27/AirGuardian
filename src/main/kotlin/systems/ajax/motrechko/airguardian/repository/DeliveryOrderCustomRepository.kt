package systems.ajax.motrechko.airguardian.repository

import systems.ajax.motrechko.airguardian.model.Drone

interface DeliveryOrderCustomRepository {
    fun findAvailableDronesToDelivery(totalWeight: Double): List<Drone>?
}
