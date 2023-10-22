package systems.ajax.motrechko.airguardian

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
import systems.ajax.motrechko.airguardian.commonresponse.drone.Drone
import systems.ajax.motrechko.airguardian.dto.response.toProtoDrone
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.drone.get_all.proto.GetAllDronesResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.repository.DroneRepository
import java.time.Duration

@SpringBootTest
@ActiveProfiles("dev")
class TestNats {

    @Autowired
    private lateinit var natsConnection: Connection

    @Autowired
    private lateinit var reactiveMongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var droneRepository: DroneRepository

    @AfterEach
    fun cleanDB() {
        reactiveMongoTemplate.remove(Query(), Drone::class.java).block()
    }


    @Test
    fun `should return success response for get all devices`() {
        // GIVEN
        val request = GetAllDronesRequest.getDefaultInstance()

        val protoDeviceList = droneRepository.findAll().map { it.toResponse().toProtoDrone() }
            .collectList()
            .block()

        println(protoDeviceList)

        val expectedResponse = GetAllDronesResponse.newBuilder().apply {
            successBuilder.addAllDrones(protoDeviceList)
        }.build()
        println("this is expected response: $expectedResponse")
        // WHEN
        val actual = doRequest(
            NatsSubject.EmergencyRequest.GET_ALL,
            request,
            GetAllDronesResponse.parser()
        )

        // THEN
        assertThat(actual).isEqualTo(expectedResponse)
    }

    private fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3> doRequest(
        subject: String,
        payload: RequestT,
        parser: Parser<ResponseT>,
    ): ResponseT {
        val response = natsConnection.requestWithTimeout(
            subject,
            payload.toByteArray(),
            Duration.ofSeconds(10L)
        )
        return parser.parseFrom(response.get().data)
    }
}
