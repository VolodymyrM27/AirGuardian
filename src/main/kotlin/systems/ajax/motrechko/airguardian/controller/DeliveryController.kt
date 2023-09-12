package systems.ajax.motrechko.airguardian.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import systems.ajax.motrechko.airguardian.dto.request.OrderCreateRequest
import systems.ajax.motrechko.airguardian.dto.request.toEntity
import systems.ajax.motrechko.airguardian.service.DeliveryOrderService

@RestController
@RequestMapping("/api/V1/delivery")
class DeliveryController(
    private val deliveryOrderService: DeliveryOrderService,
) {
    @PostMapping
    fun createNewOrder(@RequestBody order: OrderCreateRequest) = deliveryOrderService.createNewOrder(order.toEntity())
}
