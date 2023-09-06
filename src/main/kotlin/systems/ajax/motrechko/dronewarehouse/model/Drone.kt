package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import systems.ajax.motrechko.dronewarehouse.enums.DroneType

@Document("drones")
data class Drone(
    @Id
    val id: ObjectId = ObjectId(),
    val model: String = "",
    val type: List<DroneType> = emptyList(),
    val speed: Double = 0.0,
    val weight: Double = 0.0,
    @Field("number_of_propellers")
    val numberOfPropellers: Int = 0,
    @Field("load_capacity")
    val loadCapacity: Double = 0.0,
    val cost: Double = 0.0
)
