package systems.ajax.motrechko.airguardian.utils

import systems.ajax.motrechko.airguardian.model.Drone

object BatteryCalculator {
    private const val ENERGY_CONSUMPTION_PER_KILOGRAM = 0.02
    private const val ENERGY_CONSUMPTION_PER_KILOGRAM_CARGO = 0.02
    private const val ENERGY_CONSUMPTION_PER_PROPELLER = 3.5
    private const val METERS_IN_KILOMETERS = 1000.0

    fun calculateBatteryConsumption(drone: Drone, distance: Double, cargoWeight: Double): Double {
        val distanceInKilometers = distance / METERS_IN_KILOMETERS
        val weightConsumption = ENERGY_CONSUMPTION_PER_KILOGRAM * drone.weight
        val propellerConsumption = ENERGY_CONSUMPTION_PER_PROPELLER * drone.numberOfPropellers
        val cargoConsumption = ENERGY_CONSUMPTION_PER_KILOGRAM_CARGO * cargoWeight
        val speedFactor = 1 + (drone.speed - 1) / 2.0

        return (weightConsumption + propellerConsumption) * speedFactor * distanceInKilometers + cargoConsumption
    }
}
