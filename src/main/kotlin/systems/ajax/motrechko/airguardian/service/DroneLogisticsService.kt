package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.DeliveryItem
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.model.getTotalWeight
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderMongoRepository
import systems.ajax.motrechko.airguardian.utils.BatteryCalculator
import systems.ajax.motrechko.airguardian.utils.CoordinatesUtils
import systems.ajax.motrechko.airguardian.utils.FlightRecordUtils
import java.time.LocalDateTime

@Service
class DroneLogisticsService(
    private val deliveryOrderCustomRepository: DeliveryOrderMongoRepository,
    private val droneService: DroneService
) {
    fun findAvailableDrones(items: List<DeliveryItem>): List<Drone> {
        val totalWeight = items.getTotalWeight()
        logger.debug("total weight of items {}", totalWeight)
        val dronesForTotalCargo = findDronesForTotalCargo(totalWeight)

        if (dronesForTotalCargo.isNotEmpty()) {
            return dronesForTotalCargo
        }

        return findDronesForCategories(items)
    }

    private fun findDronesForTotalCargo(totalWeight: Double): List<Drone> {
        val drone = deliveryOrderCustomRepository.findAllAvailableDronesToDelivery(totalWeight)
        return if (drone.isNotEmpty())
            listOf(drone.first())
        else
            emptyList()
    }

    private fun findDronesForCategories(items: List<DeliveryItem>): List<Drone> {
        val dronesForCategories = mutableListOf<Drone>()

        for (item in items) {
            val dronesForCategory = findDronesForTotalCargo(item.weight)

            if (dronesForCategory.isNotEmpty()) {
                dronesForCategories.addAll(dronesForCategory)
            }
        }

        return if (dronesForCategories.size == items.size)
            dronesForCategories
        else
            emptyList()
    }

    fun initializeOrderForDelivery(order: DeliveryOrder, drones: List<Drone>) {
        val currentTime = LocalDateTime.now()
        val randomStartPosition = CoordinatesUtils.generateRandomCoordinatesWithinRange(
            order.deliveryCoordinates.latitude,
            order.deliveryCoordinates.longitude,
            PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS
        )

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
        val batteryConsumption =
            BatteryCalculator.calculateBatteryConsumption(drone, distance, order.items.getTotalWeight())
        logger.debug("battery Consumption for delivery is {}", batteryConsumption)
        logger.debug("distance for delivery is {}", distance)

        drone.status = DroneStatus.BUSY
        drone.batteryLevel -= batteryConsumption
        drone.flightHistory += FlightRecordUtils.createFlightRecord(
            currentTime,
            randomStartPosition,
            order.deliveryCoordinates
        )
        droneService.updateDroneInfo(drone)

        order.deliveryDroneIds += drone.id.toHexString()
        order.status = DeliveryStatus.IN_PROGRESS
    }

    companion object{
        private const val PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS: Double = 25.0
        private val logger: Logger = LoggerFactory.getLogger(DroneLogisticsService::class.java)
    }
}
