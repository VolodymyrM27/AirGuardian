package systems.ajax.motrechko.airguardian


import io.nats.client.Connection
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.ActiveProfiles
import systems.ajax.motrechko.airguardian.core.infrastructure.mapper.toProtoTimestampBuilder
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.drone.application.port.DroneRepositoryOutPort
import systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity.MongoDrone
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventStatus
import systems.ajax.motrechko.airguardian.emergencyevent.domain.EmergencyEventType.SHOOTING
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.adapters.repository.entity.MongoEmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.mapper.toProtoEmergencyEvent
import systems.ajax.motrechko.airguardian.emergencyevent.infrastructure.mapper.toResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.emergencyevent.new_event.proto.EmergencyEventRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.emergencyevent.new_event.proto.EmergencyEventResponse
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.utils.TestUtils
import systems.ajax.motrechko.airguardian.utils.TestUtils.doRequest
import java.time.LocalDateTime
import com.google.type.LatLng as ProtoCoordinates
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEvent as ProtoEmergencyEvent
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventStatus as ProtoEmergencyEventStatus
import systems.ajax.motrechko.airguardian.commonresponse.event.EmergencyEventType as ProtoEmergencyEventType

@SpringBootTest
@ActiveProfiles("test")
class EmergencyEventProcessNatsControllerTest {
    @Autowired
    private lateinit var natsConnection: Connection

    @Autowired
    private lateinit var reactiveMongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var droneRepository: DroneRepositoryOutPort

    @AfterEach
    fun cleanDB() {
        reactiveMongoTemplate.remove(Query(), MongoDrone::class.java)
            .block()
        reactiveMongoTemplate.remove(Query(), MongoEmergencyEvent::class.java)
            .block()
    }

    @Test
    fun `should return success response when emergencyService process new event`() {
        // GIVEN
        setUpForSuccessCase()
        val currentTime = LocalDateTime.now()

        val protoEmergencyEvent = ProtoEmergencyEvent
            .newBuilder()
            .setId(ObjectId("123456789101123456789101").toHexString())
            .setEventType(ProtoEmergencyEventType.SHOOTING)
            .setLocation(ProtoCoordinates.newBuilder().setLatitude(40.7128).setLongitude(-74.006))
            .setTimestamp(currentTime.toProtoTimestampBuilder())
            .setDescription("Shooting in street")
            .setEventStatus(ProtoEmergencyEventStatus.NEW)

        val emergencyEventRequest = EmergencyEventRequest.newBuilder()
            .setEvent(protoEmergencyEvent)
            .build()

        val expectedEmergencyEvent = EmergencyEvent(
            id = "123456789101123456789101",
            eventType = SHOOTING,
            location = Coordinates(40.7128, -74.006),
            timestamp = currentTime,
            description = "Shooting in street",
            emergencyEventStatus = EmergencyEventStatus.DRONE_ON_THE_WAY,
            droneId = TestUtils.DRONE_TWO.id
        )


        val expectedResponse = EmergencyEventResponse.newBuilder()
            .setSuccess(
                EmergencyEventResponse.Success.newBuilder()
                    .setEvent(expectedEmergencyEvent.toResponse().toProtoEmergencyEvent())
            )
            .build()

        // WHEN
        val actual = doRequest(
            natsConnection,
            NatsSubject.EmergencyRequest.NEW_EMERGENCY_EVENT,
            emergencyEventRequest,
            EmergencyEventResponse.parser()
        )
        // THEN
        assertThat(actual).isEqualTo(expectedResponse)

    }

    @Test
    @Suppress("MaxLineLength")
    fun `should return failed response when emergencyService process new event and couldn't find the right drones`() {
        // GIVEN
        setUpForFailedCase()
        val currentTime = LocalDateTime.now()

        val protoEmergencyEvent = ProtoEmergencyEvent
            .newBuilder()
            .setId(ObjectId("123456789101123456789101").toHexString())
            .setEventType(ProtoEmergencyEventType.SHOOTING)
            .setLocation(ProtoCoordinates.newBuilder().setLatitude(40.7128).setLongitude(-74.006))
            .setTimestamp(currentTime.toProtoTimestampBuilder())
            .setDescription("Shooting in street")
            .setEventStatus(ProtoEmergencyEventStatus.NEW)

        val emergencyEventRequest = EmergencyEventRequest.newBuilder()
            .setEvent(protoEmergencyEvent)
            .build()

        val expectedResponse = EmergencyEventResponse.newBuilder()
            .setFailure(
                EmergencyEventResponse.Failure.newBuilder()
                    .setMessage(
                        "No available drones were found: DroneIsNotAvailableException: "
                                + "systems.ajax.motrechko.airguardian.core.application.exception.DroneIsNotAvailableException: " +
                                "No available drones were found for event: SHOOTING"
                    )
            )
            .build()

        // WHEN
        val actual = doRequest(
            natsConnection,
            NatsSubject.EmergencyRequest.NEW_EMERGENCY_EVENT,
            emergencyEventRequest,
            EmergencyEventResponse.parser()
        )
        // THEN
       assertThat(actual).isEqualTo(expectedResponse)
    }

    private fun setUpForSuccessCase() {
        droneRepository.save(TestUtils.DRONE_ONE)
            .then(droneRepository.save(TestUtils.DRONE_TWO))
            .then(droneRepository.save(TestUtils.DRONE_THREE))
            .block()
    }

    private fun setUpForFailedCase() {
        droneRepository.save(TestUtils.DRONE_THREE)
            .block()
    }
}
