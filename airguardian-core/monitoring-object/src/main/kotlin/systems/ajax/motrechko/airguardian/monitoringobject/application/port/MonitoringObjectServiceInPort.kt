package systems.ajax.motrechko.airguardian.monitoringobject.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObject

interface MonitoringObjectServiceInPort {

    fun findAll(): Flux<MonitoringObject>

    fun findById(id: String): Mono<MonitoringObject>

    fun deleteById(id: String): Mono<Unit>

    fun save(monitoringObject: MonitoringObject): Mono<MonitoringObject>

    fun update(monitoringObject: MonitoringObject): Mono<MonitoringObject>
}
