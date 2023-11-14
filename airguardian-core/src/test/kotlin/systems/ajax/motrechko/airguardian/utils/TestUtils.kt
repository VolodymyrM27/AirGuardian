package systems.ajax.motrechko.airguardian.utils

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneSize
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.domain.DroneType
import java.time.Duration

object TestUtils {
    fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3> doRequest(
        natsConnection: Connection,
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

    val DRONE_ONE = Drone(
        id = "111115432345678987654321",
        model = "test one",
        type = listOf(DroneType.FPV),
        speed = 50.0,
        weight = 0.5,
        numberOfPropellers = 4,
        loadCapacity = 0.0,
        cost = 200.0,
        status = DroneStatus.ACTIVE,
        batteryLevel = 100.0,
        flightHistory = emptyList(),
        size = DroneSize.MEDIUM,
        maxFlightAltitude = 30.0
    )

    val DRONE_TWO = Drone(
        id = "222225432304628987654321",
        model = "test two",
        type = listOf(DroneType.FPV, DroneType.MONITORING),
        speed = 70.0,
        weight = 0.9,
        numberOfPropellers = 6,
        loadCapacity = 0.0,
        cost = 400.0,
        status = DroneStatus.ACTIVE,
        batteryLevel = 100.0,
        flightHistory = emptyList(),
        size = DroneSize.SMALL,
        maxFlightAltitude = 30.0
    )

    val DRONE_THREE = Drone(
        id = "333335432345677268654321",
        model = "test three",
        type = listOf(DroneType.DELIVERY),
        speed = 25.0,
        weight = 4.5,
        numberOfPropellers = 4,
        loadCapacity = 20.0,
        cost = 900.0,
        status = DroneStatus.ACTIVE,
        batteryLevel = 100.0,
        flightHistory = emptyList(),
        size = DroneSize.LARGE,
        maxFlightAltitude = 50.0
    )
}
