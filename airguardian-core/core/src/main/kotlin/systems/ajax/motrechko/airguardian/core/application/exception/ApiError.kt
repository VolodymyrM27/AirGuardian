package systems.ajax.motrechko.airguardian.core.application.exception

import java.time.LocalDateTime

data class ApiError (
    val path: String,
    val message: String?,
    val statusCode: Int,
    val time: LocalDateTime
)
