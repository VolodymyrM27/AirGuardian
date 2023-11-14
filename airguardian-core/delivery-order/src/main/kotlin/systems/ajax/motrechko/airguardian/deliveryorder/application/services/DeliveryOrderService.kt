package systems.ajax.motrechko.airguardian.deliveryorder.application.services

import systems.ajax.motrechko.airguardian.core.application.exception.DeliveryOrderNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DeliveryOrderRepositoryOutPort
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DeliveryOrderServiceInPort
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DroneLogisticsServiceInPort
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus

@Service
class DeliveryOrderService(
    private val deliveryOrderRepository: DeliveryOrderRepositoryOutPort,
    private val droneLogisticsService: DroneLogisticsServiceInPort,
    private val droneRepository: DroneRepositoryOutPort
): DeliveryOrderServiceInPort {
    override fun createNewOrder(order: DeliveryOrder): Mono<DeliveryOrder> {
        return droneLogisticsService.findAvailableDrones(order.items)
            .collectList()
            .flatMap { availableDrones ->
                if (availableDrones.isNotEmpty()) {
                    initializeOrderForDeliveryAndSave(order, availableDrones)
                } else {
                    setWaitingStatusAndSave(order)
                }
            }
    }

    override fun getInfoAboutOrderByID(id: String): Mono<DeliveryOrder> = deliveryOrderRepository.findById(id)
        .switchIfEmpty(Mono.error(DeliveryOrderNotFoundException("Order with $id not found")))

    override fun findAllDeliveryOrdersByStatus(deliveryStatus: DeliveryStatus): Flux<DeliveryOrder> =
        deliveryOrderRepository.findOrderByStatus(deliveryStatus)

    override fun deleteByID(id: String) = deliveryOrderRepository.deleteById(id).thenReturn(Unit)

    override fun findAllOrdersByDroneID(droneID: String): Flux<DeliveryOrder> =
        deliveryOrderRepository.findOrdersByDroneId(droneID)

    override fun complete(id: String): Mono<DeliveryOrder> {
        return getInfoAboutOrderByID(id)
            .flatMap { setOrderAsDelivered(it) }
    }

    private fun setOrderAsDelivered(order: DeliveryOrder): Mono<DeliveryOrder> {
        return Mono.defer {
            val updatedOrder = order.copy(status = DeliveryStatus.DELIVERED)
            droneRepository.updateManyDronesStatus(
                updatedOrder.deliveryDroneIds, DroneStatus.ACTIVE
            )
                .then(deliveryOrderRepository.save(updatedOrder))
                .thenReturn(updatedOrder)
        }
    }

    private fun setWaitingStatusAndSave(order: DeliveryOrder): Mono<DeliveryOrder> {
        return  deliveryOrderRepository.save(
            order.copy(status = DeliveryStatus.WAITING_AVAILABLE_DRONES)
        )
            .doOnSuccess { logger.info("no free drones were found for prayer with id {}", order.id) }
    }

    private fun initializeOrderForDeliveryAndSave(
        order: DeliveryOrder,
        availableDrones: MutableList<Drone>
    ): Mono<DeliveryOrder> {
        return droneLogisticsService.initializeOrderForDelivery(order, availableDrones)
            .flatMap {
                deliveryOrderRepository.save(order)
            }
            .doOnSuccess {
                logger.info(
                    "find {} drones for delivery new order with id {}", availableDrones.size, order.id
                )
            }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DeliveryOrderService::class.java)
    }
}
