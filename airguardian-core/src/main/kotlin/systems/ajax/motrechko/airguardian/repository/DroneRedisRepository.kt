package systems.ajax.motrechko.airguardian.repository

import com.mongodb.client.result.DeleteResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.switchIfEmptyDeferred
import systems.ajax.motrechko.airguardian.model.Drone
import java.time.Duration

@Repository
class DroneRedisRepository(
    private val droneRepository: DroneRepository,
    private val reactiveRedisRepository: ReactiveRedisTemplate<String, Drone>,
    @Value("\${spring.data.redis.key.prefix}") private val dronePrefix: String,
    @Value("\${spring.data.redis.ttl.minutes}") private val ttlMinutes: String
) : DroneCacheableRepository {

    override fun save(entity: Drone): Mono<Drone> =
        droneRepository.save(entity)
            .flatMap { savedDrone ->
                  saveDroneToRedisCache(savedDrone)
            }

    override fun findById(id: String): Mono<Drone> {
        return reactiveRedisRepository.opsForValue()
            .get(dronePrefix + id)
            .switchIfEmpty {
                droneRepository.findById(id)
                    .flatMap { drone ->
                        saveDroneToRedisCache(drone)
                    }
            }
    }

    override fun findAll(): Flux<Drone> {
        return reactiveRedisRepository.scan(
            ScanOptions
                .scanOptions()
                .match("${dronePrefix}*")
                .build()
        ).flatMap { reactiveRedisRepository.opsForValue().get(it) }
            .switchIfEmptyDeferred {
                droneRepository.findAll()
                    .flatMap {
                        saveDroneToRedisCache(it)
                    }
            }
    }

    override fun deleteById(id: String): Mono<DeleteResult> =
        reactiveRedisRepository
            .delete(dronePrefix + id)
            .then(droneRepository.deleteById(id))

    private fun saveDroneToRedisCache(drone: Drone): Mono<Drone> {
        val key = drone.id.toHexString()
        return reactiveRedisRepository.opsForValue()
                .set(
                    dronePrefix + key,
                    drone,
                    Duration.ofMinutes(ttlMinutes.toLong())
                ).thenReturn(drone)
    }
}
