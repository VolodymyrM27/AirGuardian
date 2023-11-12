package systems.ajax.motrechko.airguardian.deliveryorder.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItem
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.drone.domain.Drone

interface DroneLogisticsServiceInPort {

    fun findAvailableDrones(items: List<DeliveryItem>): Flux<Drone>

    fun initializeOrderForDelivery(order: DeliveryOrder, drones: List<Drone>): Mono<DeliveryOrder>

}
