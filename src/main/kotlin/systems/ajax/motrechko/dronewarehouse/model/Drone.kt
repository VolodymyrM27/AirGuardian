package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.dronewarehouse.enums.DroneSize
import systems.ajax.motrechko.dronewarehouse.enums.DroneStatus
import systems.ajax.motrechko.dronewarehouse.enums.DroneType

@Document("drones")
data class Drone(
    @Id
    val id: ObjectId = ObjectId(),
    val model: String = "",
    val type: List<DroneType> = emptyList(),
    val speed: Double = 0.0,
    val weight: Double = 0.0,
    val numberOfPropellers: Int = 0,
    val loadCapacity: Double = 0.0,
    val cost: Double = 0.0,
    val status: DroneStatus = DroneStatus.INACTIVE,
    val batteryLevel: Double = 100.0,
    val flightHistory: List<FlightRecord> = emptyList(),
    val size: DroneSize = DroneSize.MEDIUM,
    val maxFlightAltitude: Double = 0.0
)
