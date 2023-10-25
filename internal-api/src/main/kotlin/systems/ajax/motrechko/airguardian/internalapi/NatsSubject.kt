package systems.ajax.motrechko.airguardian.internalapi

object NatsSubject {
    private const val REQUEST_PREFIX = "systems.ajax.motrechko.airguardian.input.request"

    private const val EVENT_PREFIX = "systems.ajax.motrechko.airguardian.output.pubsub"

    object EmergencyRequest {
        private const val EMERGENCY_PREFIX = "$REQUEST_PREFIX.emergency"

        const val GET_ALL = "$EMERGENCY_PREFIX.get_all"
        const val NEW_EMERGENCY_EVENT = "$EMERGENCY_PREFIX.new_event"
    }
}
