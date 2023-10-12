package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.dto.response.DeliveryOrderResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DeliveryOrderNotFoundException
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderMongoReactiveRepository
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderRepository
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DeliveryOrderService(
    private val deliveryOrderRepository: DeliveryOrderRepository,
    private val deliveryOrderCustomRepository: DeliveryOrderMongoReactiveRepository,
    private val droneLogisticsService: DroneLogisticsService,
    private val droneRepository: DroneRepository
) {
    fun createNewOrder(order: DeliveryOrder): Mono<DeliveryOrder> {
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

    fun getInfoAboutOrderByID(id: String): Mono<DeliveryOrder> = deliveryOrderRepository.findById(id)
        .switchIfEmpty(Mono.error(DeliveryOrderNotFoundException("Order with $id not found")))

    fun findAllDeliveryOrdersByStatus(deliveryStatus: DeliveryStatus): Mono<List<DeliveryOrderResponse>> =
        deliveryOrderCustomRepository.findOrderByStatus(deliveryStatus)
            .collectList()
            .map { orders -> orders.map { it.toResponse() } }

    fun deleteByID(id: String) = deliveryOrderCustomRepository.deleteByID(id)

    fun findAllOrdersByDroneID(droneID: String): Mono<List<DeliveryOrder>> =
        deliveryOrderCustomRepository
            .findOrdersByDroneId(droneID)
            .collectList()

    fun complete(id: String): Mono<DeliveryOrder> {
        return getInfoAboutOrderByID(id)
            .flatMap { setOrderAsDelivered(it) }
    }

    private fun setOrderAsDelivered(order: DeliveryOrder): Mono<DeliveryOrder> {
        order.status = DeliveryStatus.DELIVERED
        return droneRepository.updateManyDronesStatus(
            order.deliveryDroneIds, DroneStatus.ACTIVE
        )
            .then(deliveryOrderCustomRepository.save(order))
            .thenReturn(order)
    }

    private fun setWaitingStatusAndSave(order: DeliveryOrder): Mono<DeliveryOrder> {
       return Mono.defer {
           order.status = DeliveryStatus.WAITING_AVAILABLE_DRONES
           deliveryOrderRepository.save(order)
               .thenReturn(order)
               .doOnSuccess { logger.info("no free drones were found for prayer with id {}", order.id) }
       }
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
