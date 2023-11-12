package systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItem
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryItemType

data class DeliveryItemRequest(
    @field:NotBlank(message = "productName must not be blank")
    val productName: String,
    @field:NotBlank(message = "deliveryItemType must not be blank")
    val deliveryItemType: DeliveryItemType,
    @field:Min(value = 1, message = "quantity must be at least 1")
    val quantity: Int,
    val weight: Double
)

fun DeliveryItemRequest.toEntity() = DeliveryItem(
    productName = productName,
    deliveryItemType = deliveryItemType,
    quantity = quantity,
    weight = weight
)
