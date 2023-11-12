package systems.ajax.motrechko.airguardian.deliveryorder.domain

data class DeliveryItem(
    val productName: String,
    val deliveryItemType: DeliveryItemType,
    val quantity: Int,
    val weight: Double
)

fun List<DeliveryItem>.getTotalWeight(): Double = sumOf { it.weight }
