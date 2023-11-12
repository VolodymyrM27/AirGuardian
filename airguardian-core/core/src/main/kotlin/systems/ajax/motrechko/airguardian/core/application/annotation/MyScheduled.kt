package systems.ajax.motrechko.airguardian.core.application.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyScheduled(
    val period: Long,
    val delay: Long
)
