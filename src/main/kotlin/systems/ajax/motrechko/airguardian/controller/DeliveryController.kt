package systems.ajax.motrechko.airguardian.controller

import com.mongodb.client.result.UpdateResult
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.dto.request.OrderCreateRequest
import systems.ajax.motrechko.airguardian.dto.request.StatusRequest
import systems.ajax.motrechko.airguardian.dto.request.toEntity
import systems.ajax.motrechko.airguardian.dto.response.DeliveryOrderResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DeliveryStatus
import systems.ajax.motrechko.airguardian.model.DeliveryOrder
import systems.ajax.motrechko.airguardian.service.DeliveryOrderService

@RestController
@RequestMapping("/api/V1/delivery")
class DeliveryController(
    private val deliveryOrderService: DeliveryOrderService,
) {
    @PostMapping
    fun createNewOrder(@RequestBody order: OrderCreateRequest): Mono<DeliveryOrderResponse> =
        deliveryOrderService.createNewOrder(order.toEntity()).map { it.toResponse() }

    @GetMapping("/{id}")
    fun getInfoAboutOrderByID(@PathVariable id: String): Mono<DeliveryOrderResponse> =
        deliveryOrderService.getInfoAboutOrderByID(id).map { it.toResponse() }

    @GetMapping("/status")
    fun findAllDeliveryOrderByStatus(
        @RequestBody deliveryStatus: StatusRequest
    ): Flux<DeliveryOrderResponse> =
        deliveryOrderService.findAllDeliveryOrdersByStatus(DeliveryStatus.valueOf(deliveryStatus.status))
            .map { it.toResponse() }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    fun deleteOrderByID(@PathVariable id: String): Mono<UpdateResult> {
        return deliveryOrderService.deleteByID(id)
    }

    @GetMapping("/drone/{droneID}")
    fun getAllOrdersByDroneID(@PathVariable droneID: String): Flux<DeliveryOrder> =
        deliveryOrderService.findAllOrdersByDroneID(droneID)

    @GetMapping("/{id}/complete")
    fun completeDeliveryOrder(@PathVariable id: String): Mono<DeliveryOrder> = deliveryOrderService.complete(id)
}
