package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.nats.subscriber

import com.google.protobuf.GeneratedMessageV3
import systems.ajax.motrechko.airguardian.core.infrastructure.adapters.nats.EventNatsSubscriber
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication

interface BatteryApplicationNatsSubscriber<Event : GeneratedMessageV3> : EventNatsSubscriber<Event> {
    fun publishEvent(application: DroneBatteryChargingApplication) : Mono<Unit>
}
