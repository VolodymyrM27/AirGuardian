package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DroneService(
    private val droneRepository: DroneRepository
) {
    fun getAllDrones(): Flux<Drone> = droneRepository.findAll()

    fun getDroneById(id: String): Mono<Drone> =
        droneRepository.findById(id).switchIfEmpty(Mono.error(DroneNotFoundException("Drone with id $id not found")))

    fun createDrone(drone: Drone): Mono<Drone> = droneRepository.save(drone)

    fun deleteDroneById(id: String): Mono<Unit> {
        return droneRepository
            .deleteById(id)
            .flatMap { deletedResult ->
                if (deletedResult.deletedCount > 0) {
                    Mono.just(Unit)
                } else {
                    Mono.error(DroneNotFoundException("Drone with id $id not found"))
                }
            }
    }

    fun findDroneByStatus(droneStatus: DroneStatus): Flux<Drone> = droneRepository.findAllByStatus(droneStatus)

    fun updateDroneInfo(drone: Drone): Mono<Drone> = this.createDrone(drone)
}
