package systems.ajax.motrechko.airguardian.emergencyevent.domain

enum class EmergencyEventStatus {
    NEW,
    PROCESSING,
    SEARCH_DRONE,
    DRONE_ON_THE_WAY,
    UNDER_SUPERVISION,
    FAKE,
    FINISHED,
}
