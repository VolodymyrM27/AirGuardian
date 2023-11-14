package systems.ajax.motrechko.airguardian.deliveryorder.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus

interface DeliveryOrderServiceInPort {

    fun createNewOrder(order: DeliveryOrder): Mono<DeliveryOrder>

    fun getInfoAboutOrderByID(id: String): Mono<DeliveryOrder>

    fun findAllDeliveryOrdersByStatus(deliveryStatus: DeliveryStatus): Flux<DeliveryOrder>

    fun deleteByID(id: String): Mono<Unit>

    fun findAllOrdersByDroneID(droneID: String): Flux<DeliveryOrder>

    fun complete(id: String): Mono<DeliveryOrder>
}
