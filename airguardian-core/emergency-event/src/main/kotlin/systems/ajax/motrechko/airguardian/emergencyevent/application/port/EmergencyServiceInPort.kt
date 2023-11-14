package systems.ajax.motrechko.airguardian.emergencyevent.application.port

import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEvent

interface EmergencyServiceInPort {

    fun processEmergencyEventScheduler()

    fun processEmergencyEvent(emergencyEvent: EmergencyEvent): Mono<EmergencyEvent>

    fun initializeDronesForEmergencyEvent(emergencyEvent: EmergencyEvent, drone: Drone): Mono<Drone>

}
