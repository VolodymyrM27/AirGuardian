package systems.ajax.motrechko.airguardian.dto.response

import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.Coordinates
import systems.ajax.motrechko.airguardian.model.DeliveryItem
import systems.ajax.motrechko.airguardian.model.DeliveryOrder

data class DeliveryOrderResponse(
    val customerName: String = "",
    val deliveryAddress: String = "",
    val deliveryCoordinates: Coordinates,
    var items: List<DeliveryItem> = emptyList(),
    var status: DeliveryStatus = DeliveryStatus.PENDING,
    var deliveryDrone: List<DroneResponse>? = emptyList()
)

fun DeliveryOrder.toResponse() = DeliveryOrderResponse(
    customerName = customerName,
    deliveryAddress = deliveryAddress,
    deliveryCoordinates = deliveryCoordinates,
    items = items,
    status = status,
    deliveryDrone = deliveryDrone?.toResponse()
)
