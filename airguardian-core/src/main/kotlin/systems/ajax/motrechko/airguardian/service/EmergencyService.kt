package systems.ajax.motrechko.airguardian.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.model.EmergencyEvent
import systems.ajax.motrechko.airguardian.repository.EmergencyEventReactiveCustomRepository
import org.slf4j.LoggerFactory
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.exception.DroneIsNotAvailableException

@Service
class EmergencyService(
    private val emergencyEventRepository: EmergencyEventReactiveCustomRepository,
    private val droneLogisticsService: DroneLogisticsService
) {
    @Scheduled(fixedDelay = 60000)
    fun processEmergencyEventScheduler() {
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

    fun processEmergencyEvent(emergencyEvent: EmergencyEvent): Mono<EmergencyEvent> {
        return changeEmergencyEventStatus(emergencyEvent, EmergencyEventStatus.SEARCH_DRONE)
            .then(findAvailableDroneForEmergencyEvent())
            .switchIfEmpty {
                handleNotAvailableDroneForEmergencyEvent(emergencyEvent)
            }
            .flatMap { availableDrone ->
                droneLogisticsService.initializeDronesForEmergencyEvent(emergencyEvent, availableDrone)
            }
            .flatMap { val updatedEvent = emergencyEvent.copy(droneId = it.id)
                emergencyEventRepository.save(updatedEvent)
            }
            .flatMap { changeEmergencyEventStatus(it, EmergencyEventStatus.DRONE_ON_THE_WAY) }
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
        private val logger = LoggerFactory.getLogger(EmergencyService::class.java)
    }
}
