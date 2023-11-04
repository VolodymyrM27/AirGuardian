package systems.ajax.motrechko.airguardian.repository

import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.model.Drone

interface DroneCacheableRepository {
    fun save(entity: Drone): Mono<Drone>

    fun findById(id: String): Mono<Drone>

    fun findAll(): Flux<Drone>

    fun deleteById(id: String): Mono<DeleteResult>
}
