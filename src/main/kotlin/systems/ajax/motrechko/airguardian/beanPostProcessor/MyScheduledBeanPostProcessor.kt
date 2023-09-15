package systems.ajax.motrechko.airguardian.beanPostProcessor

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import java.util.*
import kotlin.concurrent.fixedRateTimer

@Component
class MyScheduledBeanPostProcessor : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean.javaClass
        val methods = beanClass.methods

        for (method in methods) {
            if (method.isAnnotationPresent(MyScheduled::class.java)) {
                val scheduledAnnotation = method.getAnnotation(MyScheduled::class.java)
                val delay = scheduledAnnotation.delay
                val period = scheduledAnnotation.period

                val timerTask = fixedRateTimer("MyScheduledTask", true, delay, period) {
                    method.invoke(bean)
                }

                Runtime.getRuntime().addShutdownHook(Thread {
                    timerTask.cancel()
                })
            }
        }
        return bean
    }
}
