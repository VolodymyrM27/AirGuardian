package systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItem
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus

@Document("delivery_order")
data class DeliveryOrderMongo(
    @Id
    val id: ObjectId,
    val customerName: String,
    val deliveryAddress: String,
    val deliveryCoordinates: Coordinates,
    var items: List<DeliveryItem> = emptyList(),
    var status: DeliveryStatus,
    var deliveryDroneIds: List<String> = emptyList()
)
