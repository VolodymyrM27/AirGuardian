package systems.ajax.motrechko.airguardian.mapper

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.enums.MonitoringObjectType
import systems.ajax.motrechko.airguardian.model.MonitoringObject
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject as ProtoMonitoringObject
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObjectType as ProtoMonitoringObjectType

fun MonitoringObject.toProto(): ProtoMonitoringObject =
    ProtoMonitoringObject.newBuilder()
        .setId(id?.toHexString())
        .setName(name)
        .setType(objectType.toProto())
        .setCoordinates(coordinates.toProtoCoordinates())
        .build()


fun ProtoMonitoringObject.toEntity(): MonitoringObject {
    return MonitoringObject(
        id = if (this.hasId()) {
            ObjectId(this.id)
        } else {
            null
        },
        name = name,
        objectType = type.toEntity(),
        coordinates = coordinates.toCoordinates()
    )
}

fun MonitoringObjectType.toProto(): ProtoMonitoringObjectType {
    return when (this) {
        MonitoringObjectType.AIR_QUALITY -> ProtoMonitoringObjectType.AIR_QUALITY
        MonitoringObjectType.INFRASTRUCTURE -> ProtoMonitoringObjectType.INFRASTRUCTURE
        MonitoringObjectType.TRAFFIC -> ProtoMonitoringObjectType.TRAFFIC
        else -> ProtoMonitoringObjectType.MONITORING_OBJECT_UNSPECIFIED
    }
}

fun ProtoMonitoringObjectType.toEntity(): MonitoringObjectType {
    return when (this) {
        ProtoMonitoringObjectType.AIR_QUALITY -> MonitoringObjectType.AIR_QUALITY
        ProtoMonitoringObjectType.INFRASTRUCTURE -> MonitoringObjectType.INFRASTRUCTURE
        ProtoMonitoringObjectType.TRAFFIC -> MonitoringObjectType.TRAFFIC
        else -> MonitoringObjectType.OTHER
    }
}
