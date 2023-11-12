package systems.ajax.motrechko.airguardian.batteryapplication.application.port

import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication

interface DroneChargingApplicationProducerOutPort {
    fun sendBatteryChargingApplication(batteryApplicationProto: DroneBatteryChargingApplication): Mono<Unit>
}
