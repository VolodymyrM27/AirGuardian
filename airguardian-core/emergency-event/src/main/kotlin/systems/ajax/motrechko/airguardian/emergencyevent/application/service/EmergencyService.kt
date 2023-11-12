package systems.ajax.motrechko.airguardian.emergencyevent.application.service

import systems.ajax.motrechko.airguardian.core.application.exception.DroneIsNotAvailableException
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.core.shared.util.CoordinatesUtils
import systems.ajax.motrechko.airguardian.drone.application.port.DroneServiceInPort
import systems.ajax.motrechko.airguardian.drone.application.util.BatteryCalculator
import systems.ajax.motrechko.airguardian.drone.application.util.FlightRecordUtils
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.domain.DroneType
import systems.ajax.motrechko.airguardian.emergencyevent.application.port.EmergencyEventRepositoryOutPort
import systems.ajax.motrechko.airguardian.emergencyevent.application.port.EmergencyServiceInPort
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventStatus
import java.time.LocalDateTime

@Service
class EmergencyService(
    private val emergencyEventRepository: EmergencyEventRepositoryOutPort,
    private val droneService: DroneServiceInPort
): EmergencyServiceInPort {
    @Scheduled(fixedDelay = 60000)
    override fun processEmergencyEventScheduler() {
        val emergencyEventFlux = emergencyEventRepository.findUnprocessedEmergencyEvents()
        val countMono = emergencyEventFlux.count()

        countMono.subscribe { count ->
            logger.info("Found {} unprocessed emergency events", count)
        }

        emergencyEventFlux.collectList().flatMap {
            it.map { processEmergencyEvent(it) }
            it.toMono()
        }.subscribe()
    }

    override fun processEmergencyEvent(emergencyEvent: EmergencyEvent): Mono<EmergencyEvent> {
        return changeEmergencyEventStatus(emergencyEvent, EmergencyEventStatus.SEARCH_DRONE)
            .then(findAvailableDroneForEmergencyEvent())
            .switchIfEmpty {
                handleNotAvailableDroneForEmergencyEvent(emergencyEvent)
            }
            .flatMap { availableDrone ->
                initializeDronesForEmergencyEvent(emergencyEvent, availableDrone)
            }
            .flatMap { val updatedEvent = emergencyEvent.copy(droneId = it.id)
                emergencyEventRepository.save(updatedEvent)
            }
            .flatMap { changeEmergencyEventStatus(it, EmergencyEventStatus.DRONE_ON_THE_WAY) }
    }

    override fun initializeDronesForEmergencyEvent(emergencyEvent: EmergencyEvent, drone: Drone): Mono<Drone> {
        return Mono.fromSupplier {
            val currentTime = LocalDateTime.now()
            val randomStartPosition = CoordinatesUtils.generateRandomCoordinatesWithinRange(
                emergencyEvent.location.latitude,
                emergencyEvent.location.longitude,
                PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS_EMERGENCY
            )
            val distance = CoordinatesUtils.calculateFlightDistance(randomStartPosition, emergencyEvent.location)
            val batteryConsumption =
                BatteryCalculator.calculateBatteryConsumption(drone, distance)

            drone.copy(
                status = DroneStatus.BUSY,
                batteryLevel = drone.batteryLevel - batteryConsumption,
                flightHistory = drone.flightHistory + FlightRecordUtils.createFlightRecord(
                    currentTime,
                    randomStartPosition,
                    emergencyEvent.location
                )
            )
        }.flatMap { droneService.updateDroneInfo(it) }
    }

    private fun handleNotAvailableDroneForEmergencyEvent(emergencyEvent: EmergencyEvent): Mono<Drone> {
        logger.info(
            "No available drones were found for event: {}, id: {}",
            emergencyEvent.eventType,
            emergencyEvent.id
        )
        return DroneIsNotAvailableException(
            "No available drones were found for event: ${emergencyEvent.eventType}"
        ).toMono()
    }

    private fun findAvailableDroneForEmergencyEvent(): Mono<Drone> =
        emergencyEventRepository.findDronesForEmergencyEvent(listOf(DroneType.FPV, DroneType.MONITORING))
            .next()

    private fun changeEmergencyEventStatus(
        emergencyEvent: EmergencyEvent,
        eventStatus: EmergencyEventStatus
    ): Mono<EmergencyEvent> {
        val updatedEvent = emergencyEvent.copy(emergencyEventStatus = eventStatus)
        return emergencyEventRepository.save(updatedEvent)
    }

    companion object {
        private const val PLUG_MAX_RANGE_COORDINATES_IN_KILOMETERS_EMERGENCY: Double = 25.0
        private val logger = LoggerFactory.getLogger(EmergencyService::class.java)
    }
}
