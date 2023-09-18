package systems.ajax.motrechko.airguardian.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import systems.ajax.motrechko.airguardian.dto.request.OrderCreateRequest
import systems.ajax.motrechko.airguardian.dto.request.StatusRequest
import systems.ajax.motrechko.airguardian.dto.request.toEntity
import systems.ajax.motrechko.airguardian.dto.response.DeliveryOrderResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.service.DeliveryOrderService

@RestController
@RequestMapping("/api/V1/delivery")
class DeliveryController(
    private val deliveryOrderService: DeliveryOrderService,
) {
    @PostMapping
    fun createNewOrder(@RequestBody order: OrderCreateRequest): ResponseEntity<DeliveryOrderResponse> =
        ResponseEntity.ok(deliveryOrderService.createNewOrder(order.toEntity()).toResponse())

    @GetMapping("/{id}")
    fun getInfoAboutOrderByID(@PathVariable id: String): ResponseEntity<DeliveryOrderResponse> =
        ResponseEntity.ok(deliveryOrderService.getInfoAboutOrderByID(id).toResponse())

    @GetMapping("/status")
    fun findAllDeliveryOrderByStatus(
        @RequestBody deliveryStatus: StatusRequest
    ): ResponseEntity<List<DeliveryOrderResponse>> =
        ResponseEntity.ok(
            deliveryOrderService.findDeliveryOrderByStatus(DeliveryStatus.valueOf(deliveryStatus.status)).toResponse()
        )

    @DeleteMapping("/{id}")
    fun deleteOrderByID(@PathVariable id: String) = ResponseEntity.ok(
        deliveryOrderService.deleteByID(id)
    )

    @GetMapping("/drone/{droneID}")
    fun getAllOrdersByDroneID(@PathVariable droneID: String) = ResponseEntity.ok(
        deliveryOrderService.findAllOrdersByDroneID(droneID)
    )

    @GetMapping("/{id}/complete")
    fun completeDeliveryOrder(@PathVariable id: String) = ResponseEntity.ok(
        deliveryOrderService.complete(id)
    )
}
