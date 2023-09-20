package systems.ajax.motrechko.airguardian.repository

import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone

interface DeliveryOrderCustomRepository {
    fun findAllAvailableDronesToDelivery(totalWeight: Double): List<Drone>

    fun findOrderByStatus(status: DeliveryStatus): List<DeliveryOrder>

    fun deleteByID(id: String)

    fun findOrdersByDroneId(droneID: String): List<DeliveryOrder>

    fun findOrdersByUserId(userID: String): List<DeliveryOrder>

    fun update(order: DeliveryOrder)
}
