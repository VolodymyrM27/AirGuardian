package systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.mapper

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplication
import systems.ajax.motrechko.airguardian.batteryapplication.domain.BatteryApplicationStatus
import systems.ajax.motrechko.airguardian.batteryapplication.infrastructure.adapters.repository.model.MongoBatteryApplication
import systems.ajax.motrechko.airguardian.core.infrastructure.mapper.toProtoTimestampBuilder
import systems.ajax.motrechko.airguardian.output.pubsub.application.drone_battery_charging_application.proto.DroneBatteryChargingApplicationEventList
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.BatteryApplicationStatus as ProtoBatteryApplicationStatus
import systems.ajax.motrechko.airguardian.commonresponse.application.drone_battery_charging_application.proto.DroneBatteryChargingApplication as ProtoDroneBatteryChargingApplication


fun BatteryApplication.toProto(): ProtoDroneBatteryChargingApplication {
    return ProtoDroneBatteryChargingApplication.newBuilder()
        .setServiceMessage(serviceMessage)
        .setDroneId(droneId)
        .setTimestamp(timestamp.toProtoTimestampBuilder())
        .setStatus(status.toProto())
        .setId(id)
        .build()
}

fun BatteryApplication.toMongoBatteryApplication(): MongoBatteryApplication {
    return MongoBatteryApplication(
        if (!id.isNullOrEmpty()) ObjectId(id) else ObjectId(),
        serviceMessage = serviceMessage,
        droneId = droneId,
        timestamp = timestamp,
        status = status
    )
}

fun MongoBatteryApplication.toDomain(): BatteryApplication{
    return BatteryApplication(
        id = id.toHexString(),
        serviceMessage = serviceMessage,
        droneId = droneId,
        timestamp = timestamp,
        status = status
    )
}

fun BatteryApplication.toProtoList(): List<ProtoDroneBatteryChargingApplication> {
    return listOf(this.toProto())
}

fun List<BatteryApplication>.toProtoList(): List<ProtoDroneBatteryChargingApplication> {
    return this.map { it.toProto() }
}

fun List<BatteryApplication>.toDroneBatteryChargingApplicationEventList(): DroneBatteryChargingApplicationEventList{
    return DroneBatteryChargingApplicationEventList
        .newBuilder()
        .addAllApplications(this.map { it.toProto() })
        .build()
}

fun BatteryApplicationStatus.toProto(): ProtoBatteryApplicationStatus{
    return when (this) {
        BatteryApplicationStatus.NEW -> ProtoBatteryApplicationStatus.NEW
        BatteryApplicationStatus.IN_PROGRESS -> ProtoBatteryApplicationStatus.IN_PROGRESS
        BatteryApplicationStatus.COMPLETED -> ProtoBatteryApplicationStatus.COMPLETED
    }
}

fun ProtoBatteryApplicationStatus.toEntity(): BatteryApplicationStatus {
    return when (this) {
        ProtoBatteryApplicationStatus.NEW -> BatteryApplicationStatus.NEW
        ProtoBatteryApplicationStatus.IN_PROGRESS -> BatteryApplicationStatus.IN_PROGRESS
        ProtoBatteryApplicationStatus.COMPLETED -> BatteryApplicationStatus.COMPLETED
        else -> error("Unknown BatteryApplicationStatus: $this")
    }
}
