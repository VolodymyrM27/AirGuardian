package systems.ajax.motrechko.airguardian.model

import systems.ajax.motrechko.airguardian.enums.DeliveryItemType

data class DeliveryItem(
    val productName: String,
    val deliveryItemType: DeliveryItemType,
    val quantity: Int,
    val weight: Double
)

fun List<DeliveryItem>.getTotalWeight(): Double = this.sumOf { it.weight }

