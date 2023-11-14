package systems.ajax.motrechko.airguardian.monitoringobject.domain

import systems.ajax.motrechko.airguardian.core.shared.Coordinates

data class MonitoringObject(
    val id: String? = "",
    val name: String = "",
    val objectType: MonitoringObjectType,
    val coordinates: Coordinates
)
