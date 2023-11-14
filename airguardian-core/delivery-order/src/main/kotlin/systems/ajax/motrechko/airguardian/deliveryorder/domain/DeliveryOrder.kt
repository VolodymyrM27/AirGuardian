package systems.ajax.motrechko.airguardian.deliveryorder.domain

import systems.ajax.motrechko.airguardian.core.shared.Coordinates

data class DeliveryOrder(
    val id: String? = "",
    val customerName: String,
    val deliveryAddress: String,
    val deliveryCoordinates: Coordinates,
    var items: List<DeliveryItem> = emptyList(),
    var status: DeliveryStatus,
    var deliveryDroneIds: List<String> = emptyList()
)
