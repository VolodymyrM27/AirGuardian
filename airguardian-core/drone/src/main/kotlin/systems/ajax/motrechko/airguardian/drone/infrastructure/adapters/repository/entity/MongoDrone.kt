package systems.ajax.motrechko.airguardian.drone.infrastructure.adapters.repository.entity

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.drone.domain.Drone
import systems.ajax.motrechko.airguardian.drone.domain.DroneSize
import systems.ajax.motrechko.airguardian.drone.domain.DroneStatus
import systems.ajax.motrechko.airguardian.drone.domain.DroneType

@Document("drone")
data class MongoDrone(
    @Id
    @JsonSerialize(using = ToStringSerializer::class)
    val id: ObjectId? = ObjectId(),
    val model: String,
    val type: List<DroneType>,
    val speed: Double,
    val weight: Double,
    val numberOfPropellers: Int,
    val loadCapacity: Double,
    val cost: Double,
    var status: DroneStatus,
    var batteryLevel: Double,
    var flightHistory: List<Drone.FlightRecord> = emptyList(),
    val size: DroneSize,
    val maxFlightAltitude: Double,
)
