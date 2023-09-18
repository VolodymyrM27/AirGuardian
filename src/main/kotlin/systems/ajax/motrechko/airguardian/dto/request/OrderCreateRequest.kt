package systems.ajax.motrechko.airguardian.dto.request

import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.DeliveryItem
import systems.ajax.motrechko.airguardian.model.DeliveryOrder

data class OrderCreateRequest(
    val customerName: String = "",
    val deliveryAddress: String = "",
    val deliveryCoordinates: Coordinates,
    val items: List<DeliveryItem> = emptyList(),
    val status: DeliveryStatus = DeliveryStatus.PENDING
)

fun OrderCreateRequest.toEntity() = DeliveryOrder(
    customerName = customerName,
    deliveryAddress = deliveryAddress,
    deliveryCoordinates = deliveryCoordinates,
    items = items.toList(),
    status = status,
    deliveryDroneIDs = emptyList(),
)
