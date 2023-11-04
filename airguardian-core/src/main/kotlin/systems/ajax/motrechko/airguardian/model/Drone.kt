package systems.ajax.motrechko.airguardian.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.enums.DroneSize
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.enums.DroneType

@Document("drone")
data class Drone(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    val id: ObjectId = ObjectId(),
    val model: String = "",
    val type: List<DroneType> = emptyList(),
    val speed: Double = 0.0,
    val weight: Double = 0.0,
    val numberOfPropellers: Int = 0,
    val loadCapacity: Double = 0.0,
    val cost: Double = 0.0,
    var status: DroneStatus = DroneStatus.INACTIVE,
    var batteryLevel: Double = 100.0,
    var flightHistory: List<FlightRecord> = emptyList(),
    val size: DroneSize = DroneSize.MEDIUM,
    val maxFlightAltitude: Double = 0.0
)
