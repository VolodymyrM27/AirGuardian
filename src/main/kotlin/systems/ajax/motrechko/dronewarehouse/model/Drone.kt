package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
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
    val cost: Double = 0.0
)
