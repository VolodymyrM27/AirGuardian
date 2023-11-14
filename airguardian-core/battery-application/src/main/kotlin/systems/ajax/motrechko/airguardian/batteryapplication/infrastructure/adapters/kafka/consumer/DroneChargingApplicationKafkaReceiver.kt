package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.kafka.consumer

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.nats.subscriber.BatteryApplicationNatsSubscriber
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class DroneChargingApplicationKafkaReceiver(
    private val droneChargingApplicationKafkaConsumer: KafkaReceiver<String, DroneBatteryChargingApplicationEvent>,
    private val batteryDroneChargingApplicationEventNatsController:
    BatteryApplicationNatsSubscriber<DroneBatteryChargingApplicationEvent>
) {
    @PostConstruct
    fun subscribeToEvents() {
        droneChargingApplicationKafkaConsumer.receiveAutoAck()
            .flatMap { fluxRecord ->
                fluxRecord
                    .flatMap {
                        batteryDroneChargingApplicationEventNatsController.publishEvent(it.value().application)
                    }
            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe()
    }
}
