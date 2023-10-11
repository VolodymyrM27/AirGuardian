package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.DeliveryItem
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.model.getTotalWeight
import systems.ajax.motrechko.airguardian.repository.DeliveryOrderMongoReactiveRepository
import systems.ajax.motrechko.airguardian.repository.DroneRepository
import systems.ajax.motrechko.airguardian.utils.BatteryCalculator
import systems.ajax.motrechko.airguardian.utils.CoordinatesUtils
import systems.ajax.motrechko.airguardian.utils.FlightRecordUtils
import java.time.LocalDateTime

@Service
class DroneLogisticsService(
    private val deliveryOrderCustomRepository: DeliveryOrderMongoReactiveRepository,
    private val droneService: DroneService,
    private val droneRepository: DroneRepository
) {
    fun findAvailableDrones(items: List<DeliveryItem>): Flux<Drone> {
        val totalWeight = items.getTotalWeight()
        logger.debug("total weight of items {}", totalWeight)

        val droneForTotalCargo: Flux<Drone> =
            findDronesByWeightCargo(totalWeight)
                .next()
                .flux()

        return droneForTotalCargo
            .switchIfEmpty(findDronesForCategories(items))
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

    private fun findDronesByWeightCargo(totalWeight: Double): Flux<Drone> {
        return deliveryOrderCustomRepository
            .findAllAvailableDronesToDelivery(totalWeight)
            .switchIfEmpty(Flux.empty())
    }

    private fun findDronesForCategories(items: List<DeliveryItem>): Flux<Drone> {
       return Flux.fromIterable(items)
            .flatMap {
                findAndBookDrone(it)
            }
            .collectList()
            .flatMapMany {
                if(it.size == items.size) {
                    Flux.fromIterable(it)
                } else {
                    rollbackSelectionDrones(it)
                }
            }
    }

    private fun findAndBookDrone(it: DeliveryItem) =
        findDronesByWeightCargo(it.weight).next()
        .flatMap { drone ->
            droneRepository.updateDroneStatus(drone.id.toHexString(), DroneStatus.IN_SELECTION)
                .thenReturn(drone)
        }

    private fun rollbackSelectionDrones(it: MutableList<Drone>): Flux<Drone> =
        Flux.fromIterable(it)
            .filter { it.status == DroneStatus.IN_SELECTION }
            .map { it.id.toHexString() }
            .collectList()
            .flatMap { droneRepository.updateManyDronesStatus(it, DroneStatus.ACTIVE) }
            .thenMany(Flux.empty())

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

        drone.apply {
            status = DroneStatus.BUSY
            batteryLevel -= batteryConsumption
            flightHistory += FlightRecordUtils.createFlightRecord(
                currentTime,
                randomStartPosition,
                order.deliveryCoordinates
            )
        }
        droneService.updateDroneInfo(drone)
            .subscribe(
                { updatedDrone ->
                    order.deliveryDroneIds += updatedDrone.id.toHexString()
                    order.status = DeliveryStatus.IN_PROGRESS
                },
                { error ->
                    logger.error("Error updating drone data: ${error.message}")
                }
            )
    }

    companion object {
        private const val PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS: Double = 40.0
        private val logger: Logger = LoggerFactory.getLogger(DroneLogisticsService::class.java)
    }
}

