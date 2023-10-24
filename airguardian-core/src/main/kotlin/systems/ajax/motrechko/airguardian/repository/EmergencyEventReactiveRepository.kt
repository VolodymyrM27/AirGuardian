package systems.ajax.motrechko.airguardian.repository

import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.model.Drone

interface EmergencyEventReactiveRepository {
    fun findDronesForEmergencyEvent(droneTypes: List<DroneType>): Flux<Drone>
}
