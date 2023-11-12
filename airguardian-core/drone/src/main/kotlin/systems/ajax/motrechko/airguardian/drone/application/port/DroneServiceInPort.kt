package systems.ajax.motrechko.airguardian.drone.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus

interface DroneServiceInPort {

    fun getAllDrones(): Flux<Drone>

    fun getDroneById(id: String): Mono<Drone>

    fun createDrone(drone: Drone): Mono<Drone>

    fun deleteDroneById(id: String): Mono<Unit>

    fun findDroneByStatus(droneStatus: DroneStatus): Flux<Drone>

    fun updateDroneInfo(drone: Drone): Mono<Drone>
}
