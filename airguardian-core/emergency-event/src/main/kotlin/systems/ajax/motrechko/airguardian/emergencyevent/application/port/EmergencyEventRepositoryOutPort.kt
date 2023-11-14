package systems.ajax.motrechko.airguardian.emergencyevent.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneType
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEvent

interface EmergencyEventRepositoryOutPort {
    fun findDronesForEmergencyEvent(droneTypes: List<DroneType>): Flux<Drone>

    fun findUnprocessedEmergencyEvents(): Flux<EmergencyEvent>

    fun save(entity: EmergencyEvent): Mono<EmergencyEvent>

    fun findById(id: String): Mono<EmergencyEvent>

    fun findAll(): Flux<EmergencyEvent>

    fun deleteById(id: String): Mono<Unit>
}
