package systems.ajax.motrechko.airguardian.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus

@Document("delivery_order")
data class DeliveryOrder(
    @Id
    val id: ObjectId = ObjectId(),
    val customerName: String = "",
    val deliveryAddress: String = "",
    val deliveryCoordinates: Coordinates,
    var items: List<DeliveryItem> = emptyList(),
    var status: DeliveryStatus = DeliveryStatus.PENDING,
    var deliveryDroneIds: List<String> = emptyList()
)
