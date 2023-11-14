package systems.ajax.motrechko.airguardian.core.infrastructure.adapters.nats

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Dispatcher
import reactor.core.publisher.Flux

interface EventNatsSubscriber<Event : GeneratedMessageV3> {

    val parser: Parser<Event>

    fun subscribeToEvents(eventType: String): Flux<Event>

    val dispatcher: Dispatcher
}
