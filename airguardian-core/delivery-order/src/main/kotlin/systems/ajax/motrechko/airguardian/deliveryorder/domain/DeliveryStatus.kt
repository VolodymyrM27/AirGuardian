package systems.ajax.motrechko.airguardian.deliveryorder.domain

enum class DeliveryStatus {
    PENDING,
    IN_PROGRESS,
    DELIVERED,
    CANCELED,
    WAITING_AVAILABLE_DRONES,
    DELETED,
}
