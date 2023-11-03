package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.exception.MonitoringObjectNotFoundException
import systems.ajax.motrechko.airguardian.model.MonitoringObject
import systems.ajax.motrechko.airguardian.repository.MonitoringObjectRepository

@Service
class MonitoringObjectService(
    private val monitoringObjectRepository: MonitoringObjectRepository
) {
    fun findAll(): Flux<MonitoringObject> = monitoringObjectRepository.findAll()

    fun findById(id: String): Mono<MonitoringObject> = monitoringObjectRepository.findById(id)
        .switchIfEmpty(Mono.error(MonitoringObjectNotFoundException("Monitoring Object with id $id not found")))

    fun deleteById(id: String): Mono<Unit> = monitoringObjectRepository.deleteById(id)
        .handle { deleteResult, sink ->
            if (deleteResult.deletedCount > 0) {
                sink.next(Unit)
            } else {
                sink.error(MonitoringObjectNotFoundException("Monitoring Object with id $id not found"))
            }
        }

    fun save(monitoringObject: MonitoringObject): Mono<MonitoringObject> =
        monitoringObjectRepository.save(monitoringObject)

    fun update(monitoringObject: MonitoringObject): Mono<MonitoringObject> = this.save(monitoringObject)
}
