package systems.ajax.motrechko.airguardian.batteryapplication.application.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.DroneBatteryServiceInPort
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.DroneChargingApplicationProducerOutPort
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplicationStatus
import systems.ajax.motrechko.airguardian.core.application.annotation.MyScheduled
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import java.time.LocalDateTime

@Service
class DroneBatteryCheckService(
    private val droneBatteryApplicationService: DroneBatteryServiceInPort,
    private val droneMongoRepository: DroneRepositoryOutPort,
    private val droneChargingApplicationKafkaProducer: DroneChargingApplicationProducerOutPort,
    @Value("\${air-guardian.battery-service.battery.level.for.charging}")
    private val batteryLevelToCharging: Double
) {
    @MyScheduled(delay = 3000, period = 5000)
    fun checkTheBatteriesOfAllDrones() {
        val dronesFlux: Flux<Drone> = droneMongoRepository
            .findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
                batteryLevelToCharging,
                listOf(DroneStatus.ACTIVE, DroneStatus.INACTIVE)
            )

        dronesFlux
            .flatMap { createApplicationForCharging(it) }
            .doOnComplete { logger.info("No drones found to charge") }
            .subscribe()
    }

    private fun createApplicationForCharging(drone: Drone): Mono<Unit> {
        return droneMongoRepository.save(drone.copy(status = DroneStatus.NEED_TO_CHARGE))
            .flatMap {
                val application = BatteryApplication(
                    serviceMessage = "The drone needs to be charged, battery level is ${drone.batteryLevel}%.",
                    timestamp = LocalDateTime.now(),
                    status = BatteryApplicationStatus.NEW,
                    droneId = drone.id!!
                )
                droneChargingApplicationKafkaProducer.sendBatteryChargingApplication(application)
                    .then(droneBatteryApplicationService.saveBatteryApplication(application))
            }
            .doOnSuccess {
                logger.info(
                    "A request has been received to charge a drone with an ID {}, which is now has {}%",
                    drone.id, drone.batteryLevel
                )
            }
            .thenReturn(Unit)
            .doOnError { logger.error("Error while create an application for charging the drone with ID {}", drone.id) }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DroneBatteryCheckService::class.java)
    }
}
