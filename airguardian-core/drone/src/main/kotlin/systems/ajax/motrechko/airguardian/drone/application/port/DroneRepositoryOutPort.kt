package systems.ajax.motrechko.airguardian.drone.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus

interface DroneRepositoryOutPort {

    fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): Flux<Drone>

    fun findAllByStatus (droneStatus: DroneStatus): Flux<Drone>

    fun updateManyDronesStatus(dronesIds: List<String>, newStatus: DroneStatus): Mono<Boolean>

    fun updateDroneStatus(droneId: String, newStatus: DroneStatus): Mono<Boolean>

    fun save(entity: Drone): Mono<Drone>

    fun findById(id: String): Mono<Drone>

    fun findAll(): Flux<Drone>

    fun deleteById(id: String): Mono<Boolean>
}
