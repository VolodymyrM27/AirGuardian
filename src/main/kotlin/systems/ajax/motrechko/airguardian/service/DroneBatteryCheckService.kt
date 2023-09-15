package systems.ajax.motrechko.airguardian.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import systems.ajax.motrechko.airguardian.beanPostProcessor.MyScheduled
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DroneCustomMongoRepository
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DroneBatteryCheckService(
    private val droneCustomMongoRepository: DroneCustomMongoRepository,
    private val droneMongoRepository: DroneRepository
) {
    @MyScheduled(delay = 1000, period = 5000)
    fun checkTheBatteriesOfAllDrones() {
        val drones: List<Drone> = droneCustomMongoRepository
            .findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
                BATTERY_LEVEL_FOR_CHARGING,
                listOf(DroneStatus.ACTIVE, DroneStatus.INACTIVE)
            )
        if (drones.isNotEmpty()) {
            drones.forEach { createApplicationForCharging(it) }
        } else {
            logger.info("No drones found to charge")
        }
    }

    private fun createApplicationForCharging(drone: Drone) {
        logger.info(
            "A request has been received to charge a drone with an ID {}, which is now has {}%",
            drone.id, drone.batteryLevel
        )
        drone.status = DroneStatus.NEED_TO_CHARGE
        droneMongoRepository.save(drone)
        //TODO("implement a real application if it is needed in the future")
    }

    companion object {
        private const val BATTERY_LEVEL_FOR_CHARGING = 15.5
        private val logger: Logger = LoggerFactory.getLogger(DroneBatteryCheckService::class.java)
    }
}