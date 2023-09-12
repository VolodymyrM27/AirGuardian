package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.DeliveryItem
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.model.FlightRecord
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderRepository
import systems.ajax.motrechko.airguardian.utils.BatteryCalculator
import systems.ajax.motrechko.airguardian.utils.CoordinatesUtils
import java.time.LocalDateTime
import kotlin.time.Duration

@Service
class DeliveryOrderService(
    private val deliveryOrderRepository: DeliveryOrderRepository,
) {
    fun createNewOrder(order: DeliveryOrder) {
        val availableDrones = findAvailableDrones(order.items)

        if (availableDrones.isNotEmpty()) {
            initializeOrderForDelivery(order, availableDrones)
            deliveryOrderRepository.save(order)
        } else {
            order.status = DeliveryStatus.IN_PROGRESS
            deliveryOrderRepository.save(order)
        }
    }

    private fun initializeOrderForDelivery(order: DeliveryOrder, drones: List<Drone>) {
        val currentTime = LocalDateTime.now()
        val randomStartPosition = CoordinatesUtils.generateRandomCoordinates()

        drones.forEach { drone ->
            initializeDroneForDelivery(drone, order, currentTime, randomStartPosition)
        }
    }

    private fun initializeDroneForDelivery(
        drone: Drone,
        order: DeliveryOrder,
        currentTime: LocalDateTime,
        randomStartPosition: Coordinates
    ) {
        val distance = CoordinatesUtils.calculateFlightDistance(randomStartPosition, order.deliveryCoordinates)
        val batteryConsumption = BatteryCalculator.calculateBatteryConsumption(drone, distance)

        drone.status = DroneStatus.BUSY
        drone.batteryLevel -= batteryConsumption
        drone.flightHistory += createFlightRecord(currentTime, randomStartPosition, order.deliveryCoordinates)

        order.deliveryDrone = listOf(drone)
        order.status = DeliveryStatus.IN_PROGRESS
    }

    private fun findAvailableDrones(items: List<DeliveryItem>): List<Drone> {
        // val totalWeight = items.getTotalWeight()

        TODO("Create a basic logic for finding the right number of drones")

    }

    private fun createFlightRecord(
        startTime: LocalDateTime,
        startLocation: Coordinates,
        endLocation: Coordinates
    ): FlightRecord {
        val endTime = startTime.plusMinutes(30)
        val flightDistance = CoordinatesUtils.calculateFlightDistance(startLocation, endLocation)

        return FlightRecord(
            startTime = startTime,
            endTime = endTime,
            startLocation = startLocation,
            endLocation = endLocation,
            flightDistance = flightDistance,
            flightDuration = Duration.parse("30")
        )
    }
}
