package systems.ajax.motrechko.airguardian.controller.grpc

import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.ReactorMonitoringObjectServiceGrpc
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.create_monitoring_object.CreateMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.create_monitoring_object.CreateMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.delete_monitoring_object.DeleteMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.delete_monitoring_object.DeleteMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_object.GetMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_object.GetMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_objects.GetMonitoringObjectsRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.get_monitoring_objects.GetMonitoringObjectsResponse
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.update_monitoring_object.UpdateMonitoringObjectRequest
import systems.ajax.motrechko.airguardian.input.reqrepl.monitoringobject.update_monitoring_object.UpdateMonitoringObjectResponse
import systems.ajax.motrechko.airguardian.mapper.toEntity
import systems.ajax.motrechko.airguardian.mapper.toProto
import systems.ajax.motrechko.airguardian.service.MonitoringObjectService

@GrpcService
@Suppress("TooManyFunctions")
class MonitoringObjectGrpcService(
    private val monitoringObjectService: MonitoringObjectService
) : ReactorMonitoringObjectServiceGrpc.MonitoringObjectServiceImplBase() {
    override fun getMonitoringObject(request: Mono<GetMonitoringObjectRequest>): Mono<GetMonitoringObjectResponse> =
        request.flatMap { processFindById(it) }

    override fun getMonitoringObjects(request: Mono<GetMonitoringObjectsRequest>): Mono<GetMonitoringObjectsResponse> =
        request.flatMap { processGetAll() }

    override fun createMonitoringObject(request: Mono<CreateMonitoringObjectRequest>)
    : Mono<CreateMonitoringObjectResponse> =
        request.flatMap { processCreate(it) }
    override fun updateMonitoringObject(request: Mono<UpdateMonitoringObjectRequest>)
    : Mono<UpdateMonitoringObjectResponse> =
        request.flatMap { processUpdate(it) }

    override fun deleteMonitoringObject(request: Mono<DeleteMonitoringObjectRequest>)
            : Mono<DeleteMonitoringObjectResponse> =
        request.flatMap { processDelete(it) }

    private fun processCreate(createMonitoringObjectRequest: CreateMonitoringObjectRequest) =
        monitoringObjectService.save(createMonitoringObjectRequest.monitoringObject.toEntity())
            .map { buildSuccessResponseCreate(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseCreate(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    private fun processGetAll() =
        monitoringObjectService.findAll()
            .collectList()
            .map { monitoringObjects -> buildSuccessResponseGetAll(monitoringObjects.map { it.toProto() }) }
            .onErrorResume { exception ->
                buildFailureResponseGetAll(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    private fun processUpdate(updateMonitoringObjectRequest: UpdateMonitoringObjectRequest) =
        monitoringObjectService.update(updateMonitoringObjectRequest.monitoringObject.toEntity())
            .map { buildSuccessResponseUpdate(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseUpdate(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    private fun processFindById(it: GetMonitoringObjectRequest) =
        monitoringObjectService.findById(it.monitoringObjectId)
            .map { buildSuccessResponseGetById(it.toProto()) }
            .onErrorResume { exception ->
                buildFailureResponseGetById(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }

    private fun processDelete(deleteMonitoringObjectRequest: DeleteMonitoringObjectRequest) =
        monitoringObjectService.deleteById(deleteMonitoringObjectRequest.monitoringObjectId)
            .map { DeleteMonitoringObjectResponse.newBuilder().build() }
            .onErrorResume { exception ->
                buildFailureDeleteResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    private fun buildSuccessResponseCreate(monitoringObject: MonitoringObject): CreateMonitoringObjectResponse =
        CreateMonitoringObjectResponse.newBuilder().apply {
            successBuilder.setMonitoringObject(monitoringObject)
        }.build()

    private fun buildSuccessResponseUpdate(monitoringObject: MonitoringObject): UpdateMonitoringObjectResponse =
        UpdateMonitoringObjectResponse.newBuilder().apply {
            successBuilder.setMonitoringObject(monitoringObject)
        }.build()

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

    private fun buildFailureResponseCreate(exception: String, message: String): CreateMonitoringObjectResponse =
        CreateMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object create failed by $exception: $message")
        }.build()
    private fun buildFailureResponseUpdate(exception: String, message: String): UpdateMonitoringObjectResponse =
        UpdateMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object update failed by $exception: $message")
        }.build()
    private fun buildFailureDeleteResponse(exception: String, message: String): DeleteMonitoringObjectResponse =
        DeleteMonitoringObjectResponse.newBuilder().apply {
            failureBuilder.setMessage("Monitoring object delete failed by $exception: $message")
        }.build()
}
