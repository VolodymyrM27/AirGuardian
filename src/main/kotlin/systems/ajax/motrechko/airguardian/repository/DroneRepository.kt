package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.repository.MongoRepository
import systems.ajax.motrechko.airguardian.enums.DroneStatus
import systems.ajax.motrechko.airguardian.model.Drone

interface DroneRepository : MongoRepository<Drone, String> {
    fun findByStatus(status: DroneStatus): List<Drone>
}
