package systems.ajax.motrechko.airguardian.service

import org.springframework.stereotype.Service
import systems.ajax.motrechko.airguardian.exception.DroneNotFoundException
import systems.ajax.motrechko.airguardian.model.Drone
import systems.ajax.motrechko.airguardian.repository.DroneRepository

@Service
class DroneService(private val droneRepository: DroneRepository) {

    fun getAllDrones(): List<Drone> = droneRepository.findAll()

    fun getDroneById(id: String): Drone =
        droneRepository.findById(id).orElseThrow { DroneNotFoundException("Drone with id $id not found") }

    fun createDrone(drone: Drone): Drone = droneRepository.save(drone)

    fun deleteDroneById(id: String) = droneRepository.deleteById(id)
}
