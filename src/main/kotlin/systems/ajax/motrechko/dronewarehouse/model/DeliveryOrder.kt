package systems.ajax.motrechko.dronewarehouse.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import systems.ajax.motrechko.dronewarehouse.enums.DeliveryStatus

data class DeliveryOrder(
    @Id
    val id: ObjectId = ObjectId(),
    val customerName: String = "",
    val deliveryAddress: String = "",
    val items: List<DeliveryItem> = emptyList(),
    val status: DeliveryStatus = DeliveryStatus.PENDING
)
