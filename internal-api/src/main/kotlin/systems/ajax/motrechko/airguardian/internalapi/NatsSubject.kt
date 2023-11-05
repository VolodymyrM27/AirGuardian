package systems.ajax.motrechko.airguardian.internalapi

import systems.ajax.motrechko.airguardian.internalapi.MessageType.REQUEST_PREFIX

object NatsSubject {
    object EmergencyRequest {
        private const val EMERGENCY_PREFIX = "$REQUEST_PREFIX.emergency"

        const val GET_ALL = "$EMERGENCY_PREFIX.get_all"
        const val NEW_EMERGENCY_EVENT = "$EMERGENCY_PREFIX.new_event"
    }
}
