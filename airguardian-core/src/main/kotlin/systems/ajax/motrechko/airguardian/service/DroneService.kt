package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.dto.response.DroneResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DroneCacheableRepository
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DroneService(
    private val droneRepository: DroneRepository,
    private val droneCacheableRepository: DroneCacheableRepository
) {
    fun getAllDrones(): Flux<DroneResponse> = droneCacheableRepository
        .findAll()
        .map { it.toResponse() }

    fun getDroneById(id: String): Mono<Drone> =
        droneCacheableRepository.findById(id)
            .switchIfEmpty(Mono.error(DroneNotFoundException("Drone with id $id not found")))

    fun createDrone(drone: Drone): Mono<Drone> = droneCacheableRepository.save(drone)

    fun deleteDroneById(id: String): Mono<Unit> {
        return droneCacheableRepository.deleteById(id)
            .handle { deletedResult, sink ->
                if (deletedResult.deletedCount > 0) {
                    sink.next(Unit)
                } else {
                    sink.error(DroneNotFoundException("Drone with id $id not found"))
                }
            }
    }

    fun findDroneByStatus(droneStatus: DroneStatus): Flux<DroneResponse> =
        droneRepository.findAllByStatus(droneStatus)
            .map { it.toResponse() }

    fun updateDroneInfo(drone: Drone): Mono<Drone> = this.createDrone(drone)
}
