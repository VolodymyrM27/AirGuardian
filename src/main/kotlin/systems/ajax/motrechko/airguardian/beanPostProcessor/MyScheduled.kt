package systems.ajax.motrechko.airguardian.beanPostProcessor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyScheduled(
    val period: Long,
    val delay: Long
)
