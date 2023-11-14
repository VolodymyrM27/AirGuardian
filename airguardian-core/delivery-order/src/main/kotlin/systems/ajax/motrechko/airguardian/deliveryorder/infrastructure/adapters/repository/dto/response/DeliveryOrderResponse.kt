package systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.dto.response

import systems.ajax.motrechko.airguardian.core.shared.Coordinates
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItem
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryStatus

data class DeliveryOrderResponse(
    val customerName: String = "",
    val deliveryAddress: String = "",
    val deliveryCoordinates: Coordinates,
    var items: List<DeliveryItem> = emptyList(),
    var status: DeliveryStatus = DeliveryStatus.PENDING,
    var deliveryDrone: List<String> = emptyList()
)

fun DeliveryOrder.toResponse() = DeliveryOrderResponse(
    customerName = customerName,
    deliveryAddress = deliveryAddress,
    deliveryCoordinates = deliveryCoordinates,
    items = items,
    status = status,
    deliveryDrone = deliveryDroneIds
)

fun List<DeliveryOrder>.toResponse(): List<DeliveryOrderResponse> {
    return this.map { deliveryOrder ->
        DeliveryOrderResponse(
            customerName = deliveryOrder.customerName,
            deliveryAddress = deliveryOrder.deliveryAddress,
            deliveryCoordinates =  deliveryOrder.deliveryCoordinates,
            items = deliveryOrder.items.toList(),
            status = deliveryOrder.status,
            deliveryDrone = deliveryOrder.deliveryDroneIds.toList()
        )
    }
}
