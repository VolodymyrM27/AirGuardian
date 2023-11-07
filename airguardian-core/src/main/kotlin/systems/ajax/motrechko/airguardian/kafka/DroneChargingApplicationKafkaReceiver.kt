package systems.ajax.motrechko.airguardian.kafka

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver
import systems.ajax.motrechko.airguardian.controller.nats.BatteryDroneChargingApplicationEventNatsPublisher
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class DroneChargingApplicationKafkaReceiver(
    private val droneChargingApplicationKafkaConsumer: KafkaReceiver<String, DroneBatteryChargingApplicationEvent>,
    private val batteryDroneChargingApplicationEventNatsController:
    BatteryDroneChargingApplicationEventNatsPublisher<DroneBatteryChargingApplicationEvent>
) {
    @PostConstruct
    fun subscribeToEvents() {
        droneChargingApplicationKafkaConsumer.receiveAutoAck()
            .flatMap { fluxRecord ->
                fluxRecord
                    .map {
                        batteryDroneChargingApplicationEventNatsController.publishEvent(it.value().application)
                    }
            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe()
    }
}
