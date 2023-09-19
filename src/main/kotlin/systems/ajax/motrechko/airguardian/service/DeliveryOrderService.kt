package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DeliveryOrderNotFoundException
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderMongoRepository
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderRepository

@Service
class DeliveryOrderService(
    private val deliveryOrderRepository: DeliveryOrderRepository,
    private val deliveryOrderCustomRepository: DeliveryOrderMongoRepository,
    private val droneService: DroneService,
    private val droneLogisticsService: DroneLogisticsService
) {
    fun createNewOrder(order: DeliveryOrder): DeliveryOrder {
        val availableDrones = droneLogisticsService.findAvailableDrones(order.items)

        return if (availableDrones.isNotEmpty()) {
            droneLogisticsService.initializeOrderForDelivery(order, availableDrones)
            deliveryOrderRepository.save(order)
            logger.info("find {} drones for delivery new order with id {}", availableDrones.size, order.id)
            order
        } else {
            order.status = DeliveryStatus.IN_PROGRESS
            deliveryOrderRepository.save(order)
            logger.info("no free drones were found for prayer with id {}", order.id)
            order
        }
    }

    fun getInfoAboutOrderByID(id: String): DeliveryOrder {
        return deliveryOrderRepository.findById(id)
            .orElseThrow { DeliveryOrderNotFoundException("Order with $id not found") }
    }

    fun findDeliveryOrderByStatus(deliveryStatus: DeliveryStatus): List<DeliveryOrder> {
        return deliveryOrderCustomRepository
            .findOrderByStatus(deliveryStatus)
    }

    fun deleteByID(id: String) = deliveryOrderCustomRepository.deleteByID(id)

    fun findAllOrdersByDroneID(droneID: String): List<DeliveryOrder> =
        deliveryOrderCustomRepository.findOrdersByDroneId(droneID)

    fun complete(id: String): DeliveryOrder {
        val order = getInfoAboutOrderByID(id)
        order.status = DeliveryStatus.DELIVERED
        val drones = order.deliveryDroneIds
        drones.forEach {
            val drone = droneService.getDroneById(it)
            drone.status = DroneStatus.ACTIVE
            droneService.updateDroneInfo(drone)
        }
        deliveryOrderCustomRepository.update(order)
        return order
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DeliveryOrderService::class.java)
    }
}
