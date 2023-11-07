package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.model.BatteryApplication
import systems.ajax.motrechko.airguardian.repository.BatteryApplicationRepository

@Service
class DroneBatteryService(
    private val batteryApplicationRepository: BatteryApplicationRepository,
) {
    fun getBatteryApplications(): Flux<BatteryApplication> = batteryApplicationRepository.findAll()

    fun saveBatteryApplication(batteryApplication: BatteryApplication): Mono<BatteryApplication> =
        batteryApplicationRepository.save(batteryApplication)
}
