package systems.ajax.motrechko.airguardian.repository

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.UpdateResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone

interface DroneCustomReactiveRepository {
    fun findAllDronesWhereTheRemainingBatteryChargeIsLessThanAndHaveTheStatuses(
        batteryLevel: Double,
        statusesList: List<DroneStatus>
    ): Flux<Drone>

    fun findAllByStatus (droneStatus: DroneStatus): Flux<Drone>

    fun updateManyDronesStatus(dronesIds: List<String>, newStatus: DroneStatus): Mono<BulkWriteResult>

    fun updateDroneStatus(droneId: String, newStatus: DroneStatus): Mono<UpdateResult>
}
