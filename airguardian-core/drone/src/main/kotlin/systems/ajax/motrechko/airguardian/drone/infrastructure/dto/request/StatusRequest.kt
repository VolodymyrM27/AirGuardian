package systems.ajax.motrechko.airguardian.drone.infrastructure.dto.request

import jakarta.validation.constraints.NotBlank

data class StatusRequest(
    @field:NotBlank(message = "status must not be blank")
    val status:String = ""
)
