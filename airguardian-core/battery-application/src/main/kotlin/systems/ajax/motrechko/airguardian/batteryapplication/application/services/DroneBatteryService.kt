package systems.ajax.motrechko.airguardian.batteryapplication.application.services

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.BatteryApplicationInPort
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.DroneBatteryServiceInPort
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication

@Service
class DroneBatteryService(
    private val batteryApplicationRepository: BatteryApplicationInPort,
): DroneBatteryServiceInPort {
    override fun getBatteryApplications(): Flux<BatteryApplication> = batteryApplicationRepository.findAll()

    override fun saveBatteryApplication(batteryApplication: BatteryApplication): Mono<BatteryApplication> =
        batteryApplicationRepository.save(batteryApplication)
}
