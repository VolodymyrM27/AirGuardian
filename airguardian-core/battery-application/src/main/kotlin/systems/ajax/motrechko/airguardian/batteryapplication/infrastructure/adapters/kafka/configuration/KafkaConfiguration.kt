package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.kafka.configuration

import systems.ajax.motrechko.airguardian.core.infrastructure.configuration.kafka.GeneralKafkaConfiguration
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.sender.KafkaSender
import systems.ajax.motrechko.airguardian.internalapi.KafkaTopic
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent


@Configuration
class KafkaConfiguration(
    @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
    @Value("\${spring.kafka.properties.schema.registry.url}") schemaRegistryUrl: String
): GeneralKafkaConfiguration(bootstrapServers, schemaRegistryUrl) {
    @Bean
    fun kafkaSenderDroneBatteryChargingApplicationEvent(): KafkaSender<String, DroneBatteryChargingApplicationEvent> =
        createKafkaSender(producerProperties())

    @Bean
    fun kafkaReceiverDroneChargingApplication(): KafkaReceiver<String, DroneBatteryChargingApplicationEvent> {
        val customProperties: MutableMap<String, Any> = mutableMapOf(
            KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE to
                    DroneBatteryChargingApplicationEvent::class.java.name
        )
        return createKafkaReceiver(
            consumerProperties(customProperties),
            KafkaTopic.CHARGING_EVENT,
            "drone-application-group"
        )
    }
}
