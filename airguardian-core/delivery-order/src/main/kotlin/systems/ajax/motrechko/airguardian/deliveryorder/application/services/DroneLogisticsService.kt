package systems.ajax.motrechko.airguardian.deliveryorder.application.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.core.shared.util.CoordinatesUtils
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DeliveryOrderRepositoryOutPort
import systems.ajax.motrechko.airguardian.deliveryorder.application.port.DroneLogisticsServiceInPort
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItem
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus
import systems.ajax.motrechko.airguardian.deliveryorder.domain.getTotalWeight
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.application.port.DroneServiceInPort
import systems.ajax.motrechko.airguardian.drone.application.util.BatteryCalculator
import systems.ajax.motrechko.airguardian.drone.application.util.FlightRecordUtils
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import java.time.LocalDateTime

@Service
class DroneLogisticsService(
    private val deliveryOrderCustomRepository: DeliveryOrderRepositoryOutPort,
    private val droneService: DroneServiceInPort,
    private val droneRepository: DroneRepositoryOutPort
) : DroneLogisticsServiceInPort {
    override fun findAvailableDrones(items: List<DeliveryItem>): Flux<Drone> {
        val totalWeight = items.getTotalWeight()
        logger.debug("total weight of items {}", totalWeight)

        val droneForTotalCargo: Flux<Drone> =
            findDronesByWeightCargo(totalWeight)
                .next()
                .flux()

        return droneForTotalCargo
            .switchIfEmpty(findDronesForCategories(items))
    }

    override fun initializeOrderForDelivery(order: DeliveryOrder, drones: List<Drone>): Mono<DeliveryOrder> {
        val currentTime = LocalDateTime.now()
        val randomStartPosition = CoordinatesUtils.generateRandomCoordinatesWithinRange(
            order.deliveryCoordinates.latitude,
            order.deliveryCoordinates.longitude,
            PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS
        )

        return Flux.fromIterable(drones)
            .flatMap { drone ->
                initializeDroneForDelivery(drone, order, currentTime, randomStartPosition)
            }
            .collectList()
            .doOnNext { updatedDrones ->
                order.deliveryDroneIds += updatedDrones.map { it.id ?: "" }
                order.status = DeliveryStatus.IN_PROGRESS
            }
            .thenReturn(order)
    }

    private fun findDronesByWeightCargo(totalWeight: Double): Flux<Drone> =
        deliveryOrderCustomRepository.findAllAvailableDronesToDelivery(totalWeight)

    private fun findDronesForCategories(items: List<DeliveryItem>): Flux<Drone> {
        return Flux.fromIterable(items)
            .flatMap {
                findAndBookDrone(it)
            }
            .collectList()
            .flatMapMany {
                if (it.size == items.size) {
                    Flux.fromIterable(it)
                } else if (it.isNotEmpty()) {
                    rollbackSelectionDrones(it)
                } else {
                    Flux.empty()
                }
            }
    }

    private fun findAndBookDrone(it: DeliveryItem) =
        findDronesByWeightCargo(it.weight)
            .next()
            .flatMap { drone ->
                val updatedDrone = drone.copy(status = DroneStatus.IN_SELECTION)
                droneRepository.updateDroneStatus(drone.id ?: "", DroneStatus.IN_SELECTION) //TODO ask about this shit
                    .thenReturn(updatedDrone)
            }

    private fun rollbackSelectionDrones(it: MutableList<Drone>): Flux<Drone> =
        Flux.fromIterable(it)
            .filter {
                it.status == DroneStatus.IN_SELECTION
            }
            .map { it.id ?: "" }
            .collectList()
            .flatMap { droneRepository.updateManyDronesStatus(it, DroneStatus.ACTIVE) }
            .thenMany(Flux.empty())

    private fun initializeDroneForDelivery(
        drone: Drone,
        order: DeliveryOrder,
        currentTime: LocalDateTime,
        randomStartPosition: Coordinates
    ): Mono<Drone> {
        val distance = CoordinatesUtils.calculateFlightDistance(randomStartPosition, order.deliveryCoordinates)
        val batteryConsumption =
            BatteryCalculator.calculateBatteryConsumption(drone, distance, order.items.getTotalWeight())
        logger.debug("battery Consumption for delivery is {}", batteryConsumption)
        logger.debug("distance for delivery is {}", distance)

        val updatedDrone = drone.copy(
            status = DroneStatus.BUSY,
            batteryLevel = drone.batteryLevel - batteryConsumption,
            flightHistory = drone.flightHistory + FlightRecordUtils.createFlightRecord(
                currentTime,
                randomStartPosition,
                order.deliveryCoordinates
            )
        )

        return droneService.updateDroneInfo(updatedDrone)
    }

    companion object {
        private const val PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS: Double = 40.0
        private val logger: Logger = LoggerFactory.getLogger(DroneLogisticsService::class.java)
    }
}
