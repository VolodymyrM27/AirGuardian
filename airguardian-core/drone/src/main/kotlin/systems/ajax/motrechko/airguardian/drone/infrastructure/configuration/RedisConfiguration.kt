package systems.ajax.motrechko.airguardian.drone.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import systems.ajax.motrechko.airguardian.core.infrastructure.configuration.redis.GeneralRedisConfiguration
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.RedisDrone

@Configuration
class RedisConfiguration : GeneralRedisConfiguration<RedisDrone>(RedisDrone::class.java) {
    @Bean
    fun reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, RedisDrone> =
        createReactiveRedisTemplate(RedisDrone::class.java, connectionFactory)
}
