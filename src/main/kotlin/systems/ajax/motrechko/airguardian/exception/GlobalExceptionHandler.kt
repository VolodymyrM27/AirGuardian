package systems.ajax.motrechko.airguardian.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [DroneNotFoundException::class, DeliveryOrderNotFoundException::class])
    fun notFoundException(request: ServerHttpRequest, exception: Exception): ResponseEntity<ApiError> =
         responseEntity(request.uri.path, exception.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(request: ServerHttpRequest, exception: Exception): ResponseEntity<ApiError> =
         responseEntity(request.uri.path, exception.message, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: ServerHttpRequest
    ): ResponseEntity<ApiError> {
        val fieldErrors: List<FieldError> = ex.fieldErrors
        val errorMessage: String = fieldErrors
            .map { it.defaultMessage }
            .joinToString(", ")
        return responseEntity(request.uri.path, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun responseEntity(
        requestUri: String,
        exception: String?,
        httpStatus: HttpStatus,
    ): ResponseEntity<ApiError> {
        val errorDetail = ApiError(
            path = requestUri,
            message = exception,
            statusCode = httpStatus.value(),
            time = LocalDateTime.now()
        )
        return ResponseEntity(errorDetail, httpStatus)
    }
}
