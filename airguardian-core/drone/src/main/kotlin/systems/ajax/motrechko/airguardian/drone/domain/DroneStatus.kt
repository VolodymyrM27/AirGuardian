package systems.ajax.motrechko.airguardian.drone.domain

enum class DroneStatus {
    INACTIVE,
    ACTIVE,
    CHARGING,
    NEED_TO_CHARGE,
    BUSY,
    IN_SELECTION,
}
