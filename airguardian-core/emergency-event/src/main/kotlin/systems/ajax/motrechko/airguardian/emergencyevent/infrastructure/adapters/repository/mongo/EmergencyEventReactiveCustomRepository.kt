package systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.adapters.repository.mongo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.domain.DroneType
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toDrone
import systems.ajax.motrechko.airguardian.emergencyevent.application.port.EmergencyEventRepositoryOutPort
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.adapters.repository.entity.MongoEmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.mapper.toDomain
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.mapper.toMongoEmergencyEvent

@Repository
class EmergencyEventReactiveCustomRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : EmergencyEventRepositoryOutPort {

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
        return reactiveMongoTemplate.find(query, MongoDrone::class.java).map { it.toDrone() }
    }

    override fun findUnprocessedEmergencyEvents(): Flux<EmergencyEvent> {
        val query = Query()
            .addCriteria(Criteria.where("emergencyEventStatus").`is`(EmergencyEventStatus.SEARCH_DRONE))
        return reactiveMongoTemplate.find(query, MongoEmergencyEvent::class.java).map { it.toDomain() }
    }

    override fun save(entity: EmergencyEvent): Mono<EmergencyEvent> =
        reactiveMongoTemplate.save(entity.toMongoEmergencyEvent()).map { it.toDomain() }

    override fun findById(id: String): Mono<EmergencyEvent> =
        reactiveMongoTemplate.findById(id, MongoEmergencyEvent::class.java).map { it.toDomain() }

    override fun findAll(): Flux<EmergencyEvent> =
        reactiveMongoTemplate.findAll(MongoEmergencyEvent::class.java).map { it.toDomain() }

    override fun deleteById(id: String): Mono<Unit> =
        reactiveMongoTemplate.remove(Query(Criteria.where("_id").`is`(id)), MongoEmergencyEvent::class.java)
            .thenReturn(Unit)
}
