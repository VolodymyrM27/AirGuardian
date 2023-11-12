package systems.ajax.motrechko.airguardian.drone.infrastructure.configuration

import systems.ajax.motrechko.airguardian.core.infrastructure.configuration.redis.GeneralRedisConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone

@Configuration
class RedisConfiguration : GeneralRedisConfiguration<MongoDrone>(MongoDrone::class.java) {
    @Bean
    fun reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, MongoDrone> =
        createReactiveRedisTemplate(MongoDrone::class.java, connectionFactory)
}
