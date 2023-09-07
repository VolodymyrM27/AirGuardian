package systems.ajax.motrechko.dronewarehouse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DroneWarehouseApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<DroneWarehouseApplication>(*args)
}
