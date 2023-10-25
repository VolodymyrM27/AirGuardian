package systems.ajax.motrechko.airguardian.repository

import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone

interface DeliveryOrderReactiveRepository {
    fun findAllAvailableDronesToDelivery(totalWeight: Double): Flux<Drone>

    fun findOrderByStatus(status: DeliveryStatus): Flux<DeliveryOrder>

    fun findOrdersByDroneId(droneID: String): Flux<DeliveryOrder>

    fun findOrdersByUserId(userID: String): Flux<DeliveryOrder>
}
