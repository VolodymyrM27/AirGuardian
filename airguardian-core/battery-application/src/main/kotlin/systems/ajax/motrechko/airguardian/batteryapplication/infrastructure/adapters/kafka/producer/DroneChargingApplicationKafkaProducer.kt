package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.kafka.producer

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono
import systems.ajax.motrechko.airguardian.batteryapplication.application.port.DroneChargingApplicationProducerOutPort
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.mapper.toProto
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.internalapi.KafkaTopic
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class DroneChargingApplicationKafkaProducer(
    private val droneChargingApplicationKafkaEventSender: KafkaSender<String, DroneBatteryChargingApplicationEvent>
): DroneChargingApplicationProducerOutPort {
    override fun sendBatteryChargingApplication(batteryApplication: BatteryApplication): Mono<Unit> {
        return Mono.fromSupplier { createDroneBatteryChargingApplicationEvent(batteryApplication.toProto()) }
            .flatMap {
                droneChargingApplicationKafkaEventSender.send(createSenderRecord(batteryApplication.toProto(), it))
                    .next()
            }
            .thenReturn(Unit)
    }

    private fun createSenderRecord(
        batteryApplicationProto: DroneBatteryChargingApplication,
        droneBatteryChargingApplicationEvent: DroneBatteryChargingApplicationEvent
    ) = SenderRecord.create(
        ProducerRecord(
            KafkaTopic.CHARGING_EVENT,
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

