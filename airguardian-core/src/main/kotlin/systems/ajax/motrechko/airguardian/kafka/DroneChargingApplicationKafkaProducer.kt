package systems.ajax.motrechko.airguardian.kafka

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class DroneChargingApplicationKafkaProducer(
    private val droneChargingApplicationKafkaEventSender: KafkaSender<String, DroneBatteryChargingApplicationEvent>
) {
    fun sendBatteryChargingApplication(batteryApplicationProto: DroneBatteryChargingApplication): Mono<Unit> {
        return Mono.fromSupplier { createDroneBatteryChargingApplicationEvent(batteryApplicationProto) }
            .flatMap {
                droneChargingApplicationKafkaEventSender.send(createSenderRecord(batteryApplicationProto, it)).next()
            }
            .thenReturn(Unit)
    }

    private fun createSenderRecord(
        batteryApplicationProto: DroneBatteryChargingApplication,
        droneBatteryChargingApplicationEvent: DroneBatteryChargingApplicationEvent
    ) = SenderRecord.create(
        ProducerRecord(
            "drone-battery-charging-application",
            batteryApplicationProto.droneId,
            droneBatteryChargingApplicationEvent
        ),
        null
    ).toMono()


    private fun createDroneBatteryChargingApplicationEvent(
        batteryApplicationProto: DroneBatteryChargingApplication
    ): DroneBatteryChargingApplicationEvent =
        DroneBatteryChargingApplicationEvent.newBuilder()
            .setApplication(batteryApplicationProto)
            .build()
}

