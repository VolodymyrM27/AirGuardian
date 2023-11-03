package systems.ajax.motrechko.airguardian.service.grpc

import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_object.GetMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_object.GetMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_objects.GetMonitoringObjectsResponse
import systems.ajax.motrechko.airguardian.mapper.toProto
import systems.ajax.motrechko.airguardian.service.MonitoringObjectService

@Service
class GetMonitoringObjectService(
    private val monitoringObjectService: MonitoringObjectService
) {
    fun processGetAll() =
        monitoringObjectService.findAll()
            .collectList()
            .map { monitoringObjects -> buildSuccessResponseGetAll(monitoringObjects.map { it.toProto() }) }
            .onErrorResume { exception ->
                buildFailureResponseGetAll(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    fun processFindById(it: GetMonitoringObjectRequest) =
        monitoringObjectService.findById(it.monitoringObjectId)
            .map { buildSuccessResponseGetById(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseGetById(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }


    private fun buildSuccessResponseGetById(monitoringObject: MonitoringObject): GetMonitoringObjectResponse =
        GetMonitoringObjectResponse.newBuilder().apply {
            successBuilder.setObject(monitoringObject)
        }.build()

    private fun buildSuccessResponseGetAll(monitoringObjects: List<MonitoringObject>): GetMonitoringObjectsResponse =
        GetMonitoringObjectsResponse.newBuilder().apply {
            successBuilder.addAllMonitoringObject(monitoringObjects)
        }.build()

    private fun buildFailureResponseGetById(exception: String, message: String): GetMonitoringObjectResponse =
        GetMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object find by id failed by $exception: $message")
        }.build()

    private fun buildFailureResponseGetAll(exception: String, message: String): GetMonitoringObjectsResponse =
        GetMonitoringObjectsResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring objects find failed by $exception: $message")
        }.build()
}
