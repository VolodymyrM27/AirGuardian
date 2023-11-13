package systems.ajax.motrechko.airguardian.batteryapplication.application.port

import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication

interface DroneChargingApplicationProducerOutPort {
    fun sendBatteryChargingApplication(batteryApplicationProto: BatteryApplication): Mono<Unit>
}
