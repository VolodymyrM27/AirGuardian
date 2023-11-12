package systems.ajax.motrechko.airguardian.controller.nats

import io.nats.client.Connection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toDrone
import systems.ajax.motrechko.airguardian.drone.infrastructure.mapper.toProtoDrone
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.utils.TestUtils
import systems.ajax.motrechko.airguardian.utils.TestUtils.doRequest

@SpringBootTest
@ActiveProfiles("test")
class GetAllDronesNatsControllerTest {

    @Autowired
    private lateinit var natsConnection: Connection

    @Autowired
    private lateinit var reactiveMongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var droneRepository: DroneRepositoryOutPort

    @Autowired
    private lateinit var reactiveRedisRepository: ReactiveRedisTemplate<String,MongoDrone>

    @AfterEach
    fun cleanAfterTest() {
        reactiveRedisRepository.delete(reactiveRedisRepository.keys("*")).block()
        reactiveMongoTemplate.remove(Query(), MongoDrone::class.java).block()
    }

    @BeforeEach
    fun setUp() {
        reactiveRedisRepository.delete(reactiveRedisRepository.keys("*")).block()
        droneRepository.save(TestUtils.DRONE_ONE.toDrone())
            .then(droneRepository.save(TestUtils.DRONE_TWO.toDrone()))
            .then(droneRepository.save(TestUtils.DRONE_THREE.toDrone()))
            .block()
    }

    @Test
    fun `should return success response for get all drones`() {
        // GIVEN
        val request = GetAllDronesRequest.getDefaultInstance()

        val protoDroneList = droneRepository.findAll().map { it.toProtoDrone() }
            .collectList()
            .block()

        println(protoDroneList)

        val expectedResponse = GetAllDronesResponse.newBuilder().apply {
            successBuilder.addAllDrones(protoDroneList)
        }.build()
        // WHEN
        val actual = doRequest(
            natsConnection,
            NatsSubject.EmergencyRequest.GET_ALL,
            request,
            GetAllDronesResponse.parser()
        )
        // THEN
        assertThat(actual).isEqualTo(expectedResponse)
    }
}
