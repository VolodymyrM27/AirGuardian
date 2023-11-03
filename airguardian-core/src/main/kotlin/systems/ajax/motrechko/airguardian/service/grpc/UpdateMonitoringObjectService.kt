package systems.ajax.motrechko.airguardian.service.grpc

import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.update_monitoring_object.UpdateMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.update_monitoring_object.UpdateMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.mapper.toEntity
import systems.ajax.motrechko.airguardian.mapper.toProto
import systems.ajax.motrechko.airguardian.service.MonitoringObjectService

@Service
class UpdateMonitoringObjectService(
    private val monitoringObjectService: MonitoringObjectService
) {
    fun processUpdate(updateMonitoringObjectRequest: UpdateMonitoringObjectRequest) =
        monitoringObjectService.update(updateMonitoringObjectRequest.monitoringObject.toEntity())
            .map { buildSuccessResponseUpdate(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseUpdate(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    private fun buildSuccessResponseUpdate(monitoringObject: MonitoringObject): UpdateMonitoringObjectResponse =
        UpdateMonitoringObjectResponse.newBuilder().apply {
            successBuilder.setMonitoringObject(monitoringObject)
        }.build()

    private fun buildFailureResponseUpdate(exception: String, message: String): UpdateMonitoringObjectResponse =
        UpdateMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object update failed by $exception: $message")
        }.build()
}
