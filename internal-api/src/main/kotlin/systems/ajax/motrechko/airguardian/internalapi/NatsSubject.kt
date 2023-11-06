package systems.ajax.motrechko.airguardian.internalapi

import systems.ajax.motrechko.airguardian.internalapi.MessageType.EVENT_PREFIX
import systems.ajax.motrechko.airguardian.internalapi.MessageType.REQUEST_PREFIX

object NatsSubject {

    object BatteryDroneChargingApplication {
        private const val BATTERY_DRONE_CHARGING_APPLICATION_PREFIX =
            "$EVENT_PREFIX.battery_drone_charging_application"

        const val PUBLISH_NEW_APPLICATION = "$BATTERY_DRONE_CHARGING_APPLICATION_PREFIX.get_all"
    }

    object EmergencyRequest {
        private const val EMERGENCY_PREFIX = "$REQUEST_PREFIX.emergency"

        const val GET_ALL = "$EMERGENCY_PREFIX.get_all"
        const val NEW_EMERGENCY_EVENT = "$EMERGENCY_PREFIX.new_event"
    }
}
