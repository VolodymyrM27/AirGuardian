package systems.ajax.motrechko.airguardian.batteryapplication.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication

interface BatteryApplicationInPort {

    fun save(entity: BatteryApplication): Mono<BatteryApplication>

    fun findById(id: String): Mono<BatteryApplication>

    fun findAll(): Flux<BatteryApplication>

    fun deleteById(id: String): Mono<Boolean>
}
