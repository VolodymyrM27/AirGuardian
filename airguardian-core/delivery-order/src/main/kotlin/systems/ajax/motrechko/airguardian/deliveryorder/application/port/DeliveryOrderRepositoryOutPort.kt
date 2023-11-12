package systems.ajax.motrechko.airguardian.deliveryorder.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus
import systems.ajax.motrechko.airguardian.drone.domain.Drone

interface DeliveryOrderRepositoryOutPort{

    fun findAllAvailableDronesToDelivery(totalWeight: Double): Flux<Drone>

    fun findOrderByStatus(status: DeliveryStatus): Flux<DeliveryOrder>

    fun findOrdersByDroneId(droneID: String): Flux<DeliveryOrder>

    fun findOrdersByUserId(userID: String): Flux<DeliveryOrder>

    fun save(entity: DeliveryOrder): Mono<DeliveryOrder>

    fun findById(id: String): Mono<DeliveryOrder>

    fun findAll(): Flux<DeliveryOrder>

    fun deleteById(id: String): Mono<Boolean>
}
