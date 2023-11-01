package systems.ajax.motrechko.airguardian.mapper

import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication
import systems.ajax.motrechko.airguardian.model.BatteryApplication

fun BatteryApplication.toProto(): DroneBatteryChargingApplication {
    return DroneBatteryChargingApplication.newBuilder()
        .setServiceMessage(serviceMessage)
        .setDroneId(droneId)
        .build()
}
