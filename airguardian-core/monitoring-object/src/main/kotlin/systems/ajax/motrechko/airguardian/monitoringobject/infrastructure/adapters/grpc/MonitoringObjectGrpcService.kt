package systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc

import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.ReactorMonitoringObjectServiceGrpc
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
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc.service.CreateMonitoringObjectService
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc.service.DeleteMonitoringObjectService
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc.service.GetMonitoringObjectService
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.grpc.service.UpdateMonitoringObjectService

@GrpcService
class MonitoringObjectGrpcService(
    private val createMonitoringObjectService: CreateMonitoringObjectService,
    private val deleteMonitoringObjectService: DeleteMonitoringObjectService,
    private val updateMonitoringObjectService: UpdateMonitoringObjectService,
    private val getMonitoringObjectService: GetMonitoringObjectService
) : ReactorMonitoringObjectServiceGrpc.MonitoringObjectServiceImplBase() {
    override fun getMonitoringObject(request: Mono<GetMonitoringObjectRequest>): Mono<GetMonitoringObjectResponse> =
        request.flatMap { getMonitoringObjectService.processFindById(it) }

    override fun getMonitoringObjects(request: Mono<GetMonitoringObjectsRequest>): Mono<GetMonitoringObjectsResponse> =
        request.flatMap { getMonitoringObjectService.processGetAll() }

    override fun createMonitoringObject(request: Mono<CreateMonitoringObjectRequest>)
    : Mono<CreateMonitoringObjectResponse> =
        request.flatMap { createMonitoringObjectService.processCreate(it) }
    override fun updateMonitoringObject(request: Mono<UpdateMonitoringObjectRequest>)
    : Mono<UpdateMonitoringObjectResponse> =
        request.flatMap { updateMonitoringObjectService.processUpdate(it) }

    override fun deleteMonitoringObject(request: Mono<DeleteMonitoringObjectRequest>)
            : Mono<DeleteMonitoringObjectResponse> =
        request.flatMap { deleteMonitoringObjectService.processDelete(it) }
}
