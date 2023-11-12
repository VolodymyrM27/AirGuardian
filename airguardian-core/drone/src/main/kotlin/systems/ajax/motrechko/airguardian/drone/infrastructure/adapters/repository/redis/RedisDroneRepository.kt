package systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.redis

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.switchIfEmptyDeferred
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toMongoDrone
import java.time.Duration

@Primary
@Repository
class RedisDroneRepository(
    private val reactiveRedisRepository: ReactiveRedisTemplate<String, MongoDrone>,
    @Qualifier("mongoDroneRepository") private val droneRepository: DroneRepositoryOutPort,
    @Value("\${spring.data.redis.key.prefix}") private val dronePrefix: String,
    @Value("\${spring.data.redis.ttl.minutes}") private val ttlMinutes: String
) : DroneRepositoryOutPort by droneRepository {
    override fun save(entity: Drone): Mono<Drone> =
        droneRepository.save(entity)
            .flatMap { savedDrone ->
                saveDroneToRedisCache(savedDrone)
            }

    override fun findById(id: String): Mono<Drone> {
        return reactiveRedisRepository.opsForValue()
            .get(dronePrefix + id)
            .map { it.toDrone() }
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
        ).flatMap {
            reactiveRedisRepository.opsForValue()
                .get(it)
                .map {
                    it.toDrone()
                }
        }
            .switchIfEmptyDeferred {
                droneRepository.findAll()
                    .flatMap {
                        saveDroneToRedisCache(it)
                    }
            }
    }

    override fun deleteById(id: String): Mono<Boolean> {
        return reactiveRedisRepository
            .delete(dronePrefix + id)
            .then(droneRepository.deleteById(id))
    }

    private fun saveDroneToRedisCache(drone: Drone): Mono<Drone> {
        val key = drone.id
        return if (key != null) {
            reactiveRedisRepository.opsForValue()
                .set(
                    dronePrefix + key,
                    drone.toMongoDrone(),
                    Duration.ofMinutes(ttlMinutes.toLong())
                ).thenReturn(drone)
        } else {
            Mono.empty()
        }
    }
}
