package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.enums.DroneType
import systems.ajax.motrechko.airguardian.enums.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.model.EmergencyEvent

@Repository
class EmergencyEventReactiveCustomRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GenericRepository<EmergencyEvent>(reactiveMongoTemplate, EmergencyEvent::class.java),
    EmergencyEventReactiveRepository {
    override fun findDronesForEmergencyEvent(droneTypes: List<DroneType>): Flux<Drone> {
        val query = Query()
            .addCriteria(Criteria.where("status").`is`(DroneStatus.ACTIVE))
            .addCriteria(
                Criteria().orOperator(
                    Criteria.where("type").`in`(droneTypes),
                    Criteria.where("type").size(0)
                )
            )
            .with(
                Sort.by(Sort.Direction.DESC, "speed")
                    .and(
                        Sort.by(Sort.Direction.DESC, "batteryLevel")
                    )
            )
        return reactiveMongoTemplate.find(query, Drone::class.java)
    }

    fun findUnprocessedEmergencyEvents(): Flux<EmergencyEvent> {
        val query = Query()
            .addCriteria(Criteria.where("emergencyEventStatus").`is`(EmergencyEventStatus.SEARCH_DRONE))
        return reactiveMongoTemplate.find(query, EmergencyEvent::class.java)
    }
}
