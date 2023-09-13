package systems.ajax.motrechko.airguardian.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DroneNotFoundException::class)
    fun droneNotFound(request: HttpServletRequest, exception: DroneNotFoundException): ResponseEntity<ApiError> {
        val errorDetail = ApiError(
            path = request.requestURI,
            message = exception.message,
            statusCode = HttpStatus.NOT_FOUND.value(),
            time = LocalDateTime.now()
        )
        return ResponseEntity(errorDetail, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(request: HttpServletRequest, exception: Exception): ResponseEntity<ApiError> {
        val errorDetail = ApiError(
            path = request.requestURI,
            message = exception.message,
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            time = LocalDateTime.now()
        )
        return ResponseEntity(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val fieldErrors: List<FieldError> = ex.fieldErrors
        val errorMessage: String = fieldErrors
            .map { it.defaultMessage }
            .joinToString(", ")
        val errorDetail = ApiError(
            path = request.requestURI,
            message = errorMessage,
            statusCode = HttpStatus.BAD_REQUEST.value(),
            time = LocalDateTime.now()
        )
        return ResponseEntity(errorDetail, HttpStatus.BAD_REQUEST)
    }
}
