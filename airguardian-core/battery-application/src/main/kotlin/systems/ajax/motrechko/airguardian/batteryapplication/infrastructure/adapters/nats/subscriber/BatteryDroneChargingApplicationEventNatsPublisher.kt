package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.nats.subscriber

import com.google.protobuf.Parser
import io.nats.client.Connection
import io.nats.client.Dispatcher
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class BatteryDroneChargingApplicationEventNatsPublisher(
    private val connection: Connection
) : BatteryApplicationNatsSubscriber<DroneBatteryChargingApplicationEvent> {
    override fun publishEvent(application: DroneBatteryChargingApplication): Mono<Unit> {
        val eventMessage = DroneBatteryChargingApplicationEvent.newBuilder().apply {
            setApplication(application)
        }.build()
        return Mono.fromSupplier {
            connection.publish(
                NatsSubject.BatteryDroneChargingApplication.NEW_APPLICATION,
                eventMessage.toByteArray()
            )
        }
    }

    override val parser: Parser<DroneBatteryChargingApplicationEvent> =
        DroneBatteryChargingApplicationEvent.parser()

    override val dispatcher: Dispatcher = connection.createDispatcher()

    override fun subscribeToEvents(eventType: String): Flux<DroneBatteryChargingApplicationEvent> =
        Flux.create { sink ->
            dispatcher.apply {
                subscribe(eventType) { message ->
                    val parsedData = parser.parseFrom(message.data)
                    sink.next(parsedData)
                }
            }
        }
}
