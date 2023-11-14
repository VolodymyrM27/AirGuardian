package systems.ajax.motrechko.airguardian.monitoringobject.application.service

import systems.ajax.motrechko.airguardian.core.application.exception.MonitoringObjectNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.monitoringobject.application.port.MonitoringObjectRepositoryOutPort
import systems.ajax.motrechko.airguardian.monitoringobject.application.port.MonitoringObjectServiceInPort
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObject

@Service
class MonitoringObjectService(
    private val monitoringObjectRepository: MonitoringObjectRepositoryOutPort
): MonitoringObjectServiceInPort {
    override fun findAll(): Flux<MonitoringObject> = monitoringObjectRepository.findAll()

    override fun findById(id: String): Mono<MonitoringObject> = monitoringObjectRepository.findById(id)
        .switchIfEmpty(Mono.error(MonitoringObjectNotFoundException("Monitoring Object with id $id not found")))

    override fun deleteById(id: String): Mono<Unit> = monitoringObjectRepository.deleteById(id)
        .handle { deleteResult, sink ->
            if (deleteResult == true) {
                sink.next(Unit)
            } else {
                sink.error(MonitoringObjectNotFoundException("Monitoring Object with id $id not found"))
            }
        }

    override  fun save(monitoringObject: MonitoringObject): Mono<MonitoringObject> =
        monitoringObjectRepository.save(monitoringObject)

    override fun update(monitoringObject: MonitoringObject): Mono<MonitoringObject> = this.save(monitoringObject)
}
