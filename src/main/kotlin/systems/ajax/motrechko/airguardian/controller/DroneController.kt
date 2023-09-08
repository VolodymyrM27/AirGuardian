package systems.ajax.motrechko.airguardian.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import systems.ajax.motrechko.airguardian.dto.request.DroneCreateRequest
import systems.ajax.motrechko.airguardian.dto.request.toEntity
import systems.ajax.motrechko.airguardian.dto.response.DroneResponse
import systems.ajax.motrechko.airguardian.dto.response.toResponse
import systems.ajax.motrechko.airguardian.service.DroneService

@RestController
@RequestMapping("/api/V1/drone")
class DroneController(private val droneService: DroneService) {

    @GetMapping
    fun getAllDrone(): ResponseEntity<List<DroneResponse>> = ResponseEntity.ok(
        droneService.getAllDrones().toResponse()
    )

    @GetMapping("/{id}")
    fun getDroneById(@PathVariable id: String): ResponseEntity<DroneResponse> = ResponseEntity.ok(
        droneService.getDroneById(id).toResponse()
    )

    @DeleteMapping("/{id}")
    fun deleteDroneById(@PathVariable id: String): ResponseEntity<Unit> =
        droneService.deleteDroneById(id)
            .run { ResponseEntity.noContent().build() }

    @PostMapping
    fun createDrone(
        @RequestBody droneCreateRequest: DroneCreateRequest
    ): ResponseEntity<DroneResponse> {
        val drone = droneService.createDrone(droneCreateRequest.toEntity())
        return ResponseEntity.ok(drone.toResponse())
    }
}
