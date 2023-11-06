package systems.ajax.motrechko.airguardian.controller.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Dispatcher
import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication

interface BatteryDroneChargingApplicationEventNatsController<Event: GeneratedMessageV3> {

    val parser: Parser<Event>

    val dispatcher: Dispatcher
    fun subscribeToEvent(eventType: String): Flux<Event>

    fun publishEvent(batteryChargingApplication: DroneBatteryChargingApplication)
}
