package systems.ajax.motrechko.airguardian.controller.nats

import com.google.protobuf.Parser
import io.nats.client.Connection
import io.nats.client.Dispatcher
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.internalapi.NatsSubject
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEvent

@Component
class BatteryDroneChargingApplicationEventNatsControllerImpl(
    private val connection: Connection
) : BatteryDroneChargingApplicationEventNatsController<DroneBatteryChargingApplicationEvent> {

    override val parser: Parser<DroneBatteryChargingApplicationEvent> =
        DroneBatteryChargingApplicationEvent.parser()

    override val dispatcher: Dispatcher = connection.createDispatcher()
    override fun subscribeToEvent(eventType: String): Flux<DroneBatteryChargingApplicationEvent> =
        Flux.create { sink ->
            dispatcher.apply {
                subscribe(NatsSubject.BatteryDroneChargingApplication.GET_ALL)
                { message ->
                    val parsedData = parser.parseFrom(message.data)
                    sink.next(parsedData)
                }
            }
        }

    override fun publishEvent(batteryChargingApplication: DroneBatteryChargingApplication) {
        val eventMessage = DroneBatteryChargingApplicationEvent.newBuilder().apply {
           setApplication(batteryChargingApplication)
        }.build()

        connection.publish(NatsSubject.BatteryDroneChargingApplication.GET_ALL, eventMessage.toByteArray())
    }
}
