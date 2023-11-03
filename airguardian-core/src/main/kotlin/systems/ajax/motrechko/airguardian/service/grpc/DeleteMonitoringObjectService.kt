package systems.ajax.motrechko.airguardian.service.grpc

import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.delete_monitoring_object.DeleteMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.delete_monitoring_object.DeleteMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.service.MonitoringObjectService

@Service
class DeleteMonitoringObjectService(
    private val monitoringObjectService: MonitoringObjectService
) {
    fun processDelete(deleteMonitoringObjectRequest: DeleteMonitoringObjectRequest) =
        monitoringObjectService.deleteById(deleteMonitoringObjectRequest.monitoringObjectId)
            .map { DeleteMonitoringObjectResponse.newBuilder().build() }
            .onErrorResume { exception ->
                buildFailureDeleteResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    private fun buildFailureDeleteResponse(exception: String, message: String): DeleteMonitoringObjectResponse =
        DeleteMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object delete failed by $exception: $message")
        }.build()
}
