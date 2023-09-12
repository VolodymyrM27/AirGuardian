package systems.ajax.motrechko.airguardian.utils

import systems.ajax.motrechko.airguardian.model.Drone

object BatteryCalculator {
    private const val ENERGY_CONSUMPTION_PER_KILOGRAM = 0.02
    private const val ENERGY_CONSUMPTION_PER_PROPELLER = 0.005
    private const val METERS_IN_KILOMETERS = 1000.0

    fun calculateBatteryConsumption(drone: Drone, distance: Double): Double {
        val distanceInKilometers = distance / METERS_IN_KILOMETERS
        val weightConsumption = ENERGY_CONSUMPTION_PER_KILOGRAM * drone.weight
        val propellerConsumption = ENERGY_CONSUMPTION_PER_PROPELLER * drone.numberOfPropellers
        val speedFactor = 1 + (drone.speed - 1) / 2.0

        return (weightConsumption + propellerConsumption) * speedFactor * distanceInKilometers
    }
}
