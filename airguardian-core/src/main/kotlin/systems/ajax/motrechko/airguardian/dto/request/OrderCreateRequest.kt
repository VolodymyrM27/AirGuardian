package systems.ajax.motrechko.airguardian.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder

data class OrderCreateRequest(
    @field:NotBlank(message = "customer name must not be blank")
    val customerName: String = "",
    @field:NotBlank(message = "delivery address must not be blank")
    val deliveryAddress: String = "",
    @field:NotBlank(message = "delivery coordinates must not be blank")
    val deliveryCoordinates: CoordinatesRequest,
    @field:NotBlank(message = "items must not be blank")
    @field:Min(value = 1, message = "items must be at least 1")
    val items: List<DeliveryItemRequest> = emptyList(),
    val status: DeliveryStatus = DeliveryStatus.PENDING
)

fun OrderCreateRequest.toEntity() = DeliveryOrder(
    customerName = customerName,
    deliveryAddress = deliveryAddress,
    deliveryCoordinates = deliveryCoordinates.toEntity(),
    items = items.map { it.toEntity() }.toList(),
    status = status,
    deliveryDroneIds = emptyList(),
)
