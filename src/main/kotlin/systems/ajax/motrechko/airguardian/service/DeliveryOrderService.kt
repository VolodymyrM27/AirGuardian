package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DeliveryOrderNotFoundException
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
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
            .filter { availableDrones -> availableDrones.isNotEmpty() }
            .flatMap {availableDrones ->
                droneLogisticsService.initializeOrderForDelivery(order, availableDrones)
                deliveryOrderRepository.save(order)
                    .thenReturn(order)
                    .doOnSuccess {
                        logger.info(
                            "find {} drones for delivery new order with id {}",
                            availableDrones.size,
                            order.id
                        )
                    }
            }
            .switchIfEmpty(Mono.defer {
                order.status = DeliveryStatus.WAITING_AVAILABLE_DRONES
                deliveryOrderRepository.save(order)
                    .thenReturn(order)
                    .doOnSuccess { logger.info("no free drones were found for prayer with id {}", order.id) }
            })
    }

    fun getInfoAboutOrderByID(id: String): Mono<DeliveryOrder> =
        deliveryOrderRepository
            .findById(id)
            .switchIfEmpty(Mono.error(DeliveryOrderNotFoundException("Order with $id not found")))


    fun findDeliveryOrderByStatus(deliveryStatus: DeliveryStatus): Flux<DeliveryOrder> =
        deliveryOrderCustomRepository
            .findOrderByStatus(deliveryStatus)


    fun deleteByID(id: String) = deliveryOrderCustomRepository.deleteByID(id)

    fun findAllOrdersByDroneID(droneID: String): Flux<DeliveryOrder> =
        deliveryOrderCustomRepository.findOrdersByDroneId(droneID)

    fun complete(id: String): Mono<DeliveryOrder> {
        return getInfoAboutOrderByID(id)
            .flatMap { order ->
                order.status = DeliveryStatus.DELIVERED
                droneRepository.updateManyDronesStatus(
                    order.deliveryDroneIds,
                    DroneStatus.ACTIVE
                )
                    .flatMap { deliveryOrderCustomRepository.save(order) }
                    .thenReturn(order)
            }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DeliveryOrderService::class.java)
    }
}
