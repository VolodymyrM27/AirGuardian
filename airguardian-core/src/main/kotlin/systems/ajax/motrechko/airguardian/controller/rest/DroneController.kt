package systems.ajax.motrechko.airguardian.controller.rest

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.dto.request.DroneCreateRequest
import systems.ajax.motrechko.airguardian.dto.request.StatusRequest
import systems.ajax.motrechko.airguardian.dto.request.toEntity
import systems.ajax.motrechko.airguardian.dto.response.DroneResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.service.DroneService

@RestController
@RequestMapping("/api/V1/drone")
class DroneController(
    private val droneService: DroneService
) {
    @GetMapping
    fun getAllDrone(): Flux<DroneResponse> =
        droneService.getAllDrones()

    @GetMapping("/status")
    fun getAllDroneByStatus(@RequestBody droneStatus: StatusRequest): Flux<DroneResponse> =
        droneService.findDroneByStatus(DroneStatus.valueOf(droneStatus.status))

    @GetMapping("/{id}")
    fun getDroneById(@PathVariable id: String): Mono<DroneResponse> =
        droneService.getDroneById(id).map { it.toResponse() }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    fun deleteDroneById(@PathVariable id: String): Mono<Unit> = droneService.deleteDroneById(id)

    @PostMapping
    fun createDrone(
        @Valid @RequestBody droneCreateRequest: DroneCreateRequest
    ): Mono<DroneResponse> = droneService.createDrone(droneCreateRequest.toEntity()).map { it.toResponse() }
}
