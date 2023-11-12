package systems.ajax.motrechko.airguardian.core.infrastructure.rest.advice

import systems.ajax.motrechko.airguardian.core.application.exception.ApiError
import systems.ajax.motrechko.airguardian.core.application.exception.DeliveryOrderNotFoundException
import systems.ajax.motrechko.airguardian.core.application.exception.DroneIsNotAvailableException
import systems.ajax.motrechko.airguardian.core.application.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.core.application.exception.MonitoringObjectNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [
        DroneNotFoundException::class,
        DeliveryOrderNotFoundException::class,
        DroneIsNotAvailableException::class,
        MonitoringObjectNotFoundException::class,
    ])
    fun notFoundException(request: ServerHttpRequest, exception: Exception): Mono<ApiError> =
        responseEntity(request.uri.path, exception.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(Exception::class)
    fun handleOtherExceptions(request: ServerHttpRequest, exception: Exception): Mono<ApiError> =
        responseEntity(request.uri.path, exception.message, HttpStatus.INTERNAL_SERVER_ERROR)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: ServerHttpRequest
    ): Mono<ApiError> {
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
    ): Mono<ApiError> {
        val errorDetail = ApiError(
            path = requestUri,
            message = exception,
            statusCode = httpStatus.value(),
            time = LocalDateTime.now()
        )
        return errorDetail.toMono()
    }
}
