package systems.ajax.motrechko.airguardian.batteryapplication.application.port

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication

interface DroneBatteryServiceInPort {

    fun getBatteryApplications(): Flux<BatteryApplication>

    fun saveBatteryApplication(batteryApplication: BatteryApplication): Mono<BatteryApplication>
}
