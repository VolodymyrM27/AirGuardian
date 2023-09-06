package systems.ajax.motrechko.dronewarehouse.repository

import org.springframework.data.mongodb.repository.MongoRepository
import systems.ajax.motrechko.dronewarehouse.model.Drone

interface DroneRepository : MongoRepository<Drone, String>
