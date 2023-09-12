package systems.ajax.motrechko.airguardian

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AirGuardianApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AirGuardianApplication>(*args)
}
