package systems.ajax.motrechko.airguardian.kafka

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Component
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class DroneChargingApplicationKafkaProducer(
    private val droneChargingApplicationKafkaEvent: KafkaSender<String, DroneBatteryChargingApplicationEvent>
) {
    fun sendBatteryChargingApplication(batteryApplicationProto: DroneBatteryChargingApplication) {
        val droneBatteryChargingApplicationEvent = DroneBatteryChargingApplicationEvent.newBuilder()
            .setApplication(batteryApplicationProto)
            .build()

        val record = SenderRecord.create(
            ProducerRecord(
                "drone-battery-charging-application",
                batteryApplicationProto.droneId,
                droneBatteryChargingApplicationEvent
            ),
            null
        )

        droneChargingApplicationKafkaEvent.send(record.toMono()).subscribe()
    }
}

