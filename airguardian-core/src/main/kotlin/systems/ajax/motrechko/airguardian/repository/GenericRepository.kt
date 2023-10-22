package systems.ajax.motrechko.airguardian.repository

import com.mongodb.client.result.DeleteResult
import org.springframework.context.annotation.Lazy
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Lazy
@Component
class GenericRepository<T>(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val entityClass: Class<T>
) {
    fun save(entity: T): Mono<T> = reactiveMongoTemplate.save(entity!!)

    fun findById(id: String): Mono<T> = reactiveMongoTemplate.findById(id, entityClass)

    fun findAll(): Flux<T> = reactiveMongoTemplate.findAll(entityClass)

    fun deleteById(id: String): Mono<DeleteResult> {
        val query = Query(Criteria.where("_id").`is`(id))
        return reactiveMongoTemplate.remove(query, entityClass)
    }
}
