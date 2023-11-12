package systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc.service

import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.create_monitoring_object.CreateMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.create_monitoring_object.CreateMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.monitoringobject.application.service.MonitoringObjectService
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.mapper.toEntity
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.mapper.toProto

@Service
class CreateMonitoringObjectService(
    private val monitoringObjectService: MonitoringObjectService
) {
    fun processCreate(createMonitoringObjectRequest: CreateMonitoringObjectRequest) =
        monitoringObjectService.save(createMonitoringObjectRequest.monitoringObject.toEntity())
            .map { buildSuccessResponseCreate(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseCreate(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    private fun buildSuccessResponseCreate(monitoringObject: MonitoringObject): CreateMonitoringObjectResponse =
        CreateMonitoringObjectResponse.newBuilder().apply {
            successBuilder.setMonitoringObject(monitoringObject)
        }.build()

    private fun buildFailureResponseCreate(exception: String, message: String): CreateMonitoringObjectResponse =
        CreateMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object create failed by $exception: $message")
        }.build()
}
