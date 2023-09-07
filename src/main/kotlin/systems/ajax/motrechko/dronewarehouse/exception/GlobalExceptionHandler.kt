package systems.ajax.motrechko.dronewarehouse.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DroneNotFoundException::class)
    fun droneNotFound(request: HttpServletRequest, exception: DroneNotFoundException) : ResponseEntity<ApiError> {
        val errorDetail = ApiError(
            path = request.requestURI,
            message = exception.message,
            statusCode = HttpStatus.NOT_FOUND.value(),
            time = LocalDateTime.now()
        )
        return ResponseEntity(errorDetail, HttpStatus.NOT_FOUND)
    }

}
