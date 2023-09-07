package systems.ajax.motrechko.dronewarehouse.service

import org.springframework.stereotype.Service
import systems.ajax.motrechko.dronewarehouse.exception.DroneNotFoundException
import systems.ajax.motrechko.dronewarehouse.model.Drone
import systems.ajax.motrechko.dronewarehouse.repository.DroneRepository

@Service
class DroneService(private val droneRepository: DroneRepository) {

    fun getAllDrones(): List<Drone> = droneRepository.findAll()

    fun getDroneById(id: String): Drone =
        droneRepository.findById(id).orElseThrow { DroneNotFoundException("Drone with id $id not found") }

    fun createDrone(drone: Drone): Drone = droneRepository.save(drone)

    fun deleteDroneById(id: String) = droneRepository.deleteById(id)
}
