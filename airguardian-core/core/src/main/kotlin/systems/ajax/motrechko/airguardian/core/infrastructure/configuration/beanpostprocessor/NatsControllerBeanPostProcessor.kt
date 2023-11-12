package systems.ajax.motrechko.airguardian.core.infrastructure.configuration.beanpostprocessor

import com.google.protobuf.GeneratedMessageV3
import systems.ajax.motrechko.airguardian.core.infrastructure.adapters.nats.NatsController
import io.nats.client.Connection
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers


@Component
class NatsControllerBeanPostProcessor(
    private val connection: Connection
) : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            initializeNatsController(bean, connection)
        }
        return bean
    }

    fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3>
            initializeNatsController(controller: NatsController<RequestT, ResponseT>, connection: Connection) {
        connection.createDispatcher { message ->
            val parsedData = controller.parser.parseFrom(message.data)
            controller
                .handle(parsedData)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe { response -> connection.publish(message.replyTo, response.toByteArray()) }
        }.subscribe(controller.subject)
    }
}
