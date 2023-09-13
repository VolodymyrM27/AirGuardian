package systems.ajax.motrechko.airguardian.repository

import systems.ajax.motrechko.airguardian.model.Drone

interface DeliveryOrderCustomRepository {
    fun findAllAvailableDronesToDelivery(totalWeight: Double): List<Drone>
}
