package systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.mapper

import systems.ajax.motrechko.airguardian.core.infrastructure.mapper.toCoordinates
import systems.ajax.motrechko.airguardian.core.infrastructure.mapper.toProtoCoordinates
import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObject
import systems.ajax.motrechko.airguardian.monitoringobject.domain.MonitoringObjectType
import systems.ajax.motrechko.airguardian.monitoringobject.infrastructure.adapters.repository.entity.MongoMonitoringObject
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObject as ProtoMonitoringObject
import systems.ajax.motrechko.airguardian.commonresponse.monitoring_object.proto.MonitoringObjectType as ProtoMonitoringObjectType

fun MonitoringObject.toProto(): ProtoMonitoringObject =
    ProtoMonitoringObject.newBuilder()
        .setId(id)
        .setName(name)
        .setType(objectType.toProto())
        .setCoordinates(coordinates.toProtoCoordinates())
        .build()

fun MonitoringObject.toMongoMonitoringObject(): MongoMonitoringObject {
    return MongoMonitoringObject(
        id = id?.let { ObjectId(it) },
        name = name,
        objectType = objectType,
        coordinates = coordinates
    )
}

fun MongoMonitoringObject.toDomain(): MonitoringObject {
    return MonitoringObject(
        id = id?.toHexString(),
        name = name,
        objectType = objectType,
        coordinates = coordinates
    )
}

fun ProtoMonitoringObject.toEntity(): MonitoringObject {
    return MonitoringObject(
        id = if (this.hasId()) {
           this.id
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
