package systems.ajax.motrechko.airguardian.beanPostProcessor

import org.springframework.aop.framework.AopProxyUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class MyScheduledBeanPostProcessor : BeanPostProcessor {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(SCHEDULED_THREADS_COUNT)

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = AopProxyUtils.ultimateTargetClass(bean)
        val methods = beanClass.methods
        for (method in methods) {
            if (method.isAnnotationPresent(MyScheduled::class.java)) {
                val scheduledAnnotation = method.getAnnotation(MyScheduled::class.java)
                val delay = scheduledAnnotation.delay
                val period = scheduledAnnotation.period
                scheduler.scheduleWithFixedDelay({ method.invoke(bean) }, delay, period, TimeUnit.MILLISECONDS)
            }
        }
        return bean
    }

    companion object {
        private const val SCHEDULED_THREADS_COUNT = 10
    }
}
