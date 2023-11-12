package systems.ajax.motrechko.airguardian.drone.application.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.core.application.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.application.port.DroneServiceInPort
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus


@Service
class DroneService(
    private val droneRepository: DroneRepositoryOutPort,
) : DroneServiceInPort {
    override fun getAllDrones(): Flux<Drone> {
        return droneRepository.findAll()
    }

    override fun getDroneById(id: String): Mono<Drone> =
        droneRepository.findById(id)
            .switchIfEmpty(Mono.error(DroneNotFoundException("Drone with id $id not found")))

    override fun createDrone(drone: Drone): Mono<Drone> = droneRepository.save(drone)

    override fun deleteDroneById(id: String): Mono<Unit> {
        return droneRepository.deleteById(id)
            .handle { deletedResult, sink ->
                if (deletedResult ==  true) {
                    sink.next(Unit)
                } else {
                    sink.error(DroneNotFoundException("Drone with id $id not found"))
                }
            }
    }

    override fun findDroneByStatus(droneStatus: DroneStatus): Flux<Drone> =
        droneRepository.findAllByStatus(droneStatus)
            .map { it }

    override fun updateDroneInfo(drone: Drone): Mono<Drone> = this.createDrone(drone)
}
