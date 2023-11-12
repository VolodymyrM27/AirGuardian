package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.repository.mongo

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.BatteryApplicationInPort
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.mapper.toDomain
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.mapper.toMongoBatteryApplication
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.repository.model.MongoBatteryApplication

@Repository
class BatteryApplicationRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): BatteryApplicationInPort {
    override fun save(entity: BatteryApplication): Mono<BatteryApplication> =
        reactiveMongoTemplate.save(entity.toMongoBatteryApplication()).map { it.toDomain() }

    override fun findById(id: String): Mono<BatteryApplication> =
        reactiveMongoTemplate.findById(id, MongoBatteryApplication::class.java).map { it.toDomain() }

    override fun findAll(): Flux<BatteryApplication> =
        reactiveMongoTemplate.findAll(MongoBatteryApplication::class.java).map { it.toDomain() }

    override fun deleteById(id: String): Mono<Boolean> =
        reactiveMongoTemplate.remove(Query(Criteria.where("_id").`is`(id)), MongoBatteryApplication::class.java)
            .map { it.deletedCount > 0 }
}
