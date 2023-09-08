package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.repository.MongoRepository
import systems.ajax.motrechko.airguardian.model.Drone

interface DroneRepository : MongoRepository<Drone, String>
