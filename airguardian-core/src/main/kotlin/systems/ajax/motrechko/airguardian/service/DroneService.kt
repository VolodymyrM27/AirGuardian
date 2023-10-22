package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.dto.response.DroneResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DroneService(
    private val droneRepository: DroneRepository
) {
    fun getAllDrones(): Mono<List<DroneResponse>> = droneRepository
        .findAll()
        .map { it.toResponse() }
        .collectList()

    fun getDroneById(id: String): Mono<Drone> =
        droneRepository.findById(id).switchIfEmpty(Mono.error(DroneNotFoundException("Drone with id $id not found")))

    fun createDrone(drone: Drone): Mono<Drone> = droneRepository.save(drone)

    fun deleteDroneById(id: String): Mono<Unit> {
        return droneRepository.deleteById(id)
            .handle { deletedResult, sink ->
                if (deletedResult.deletedCount > 0) {
                    sink.next(Unit)
                } else {
                    sink.error(DroneNotFoundException("Drone with id $id not found"))
                }
            }
    }

    fun findDroneByStatus(droneStatus: DroneStatus): Mono<List<DroneResponse>> =
        droneRepository.findAllByStatus(droneStatus)
            .map { it.toResponse() }
            .collectList()

    fun updateDroneInfo(drone: Drone): Mono<Drone> = this.createDrone(drone)
}
