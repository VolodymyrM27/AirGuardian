package systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.mapper

import org.bson.types.ObjectId
import systems.ajax.motrechko.airguardian.deliveryorder.domain.DeliveryOrder
import systems.ajax.motrechko.airguardian.deliveryorder.infrastructure.adapters.repository.entity.DeliveryOrderMongo

fun DeliveryOrder.toMongoDeliveryOrder(): DeliveryOrderMongo {
    return DeliveryOrderMongo(
        if (!id.isNullOrEmpty()) ObjectId(id) else ObjectId(),
        customerName = customerName,
        deliveryAddress = deliveryAddress,
        deliveryCoordinates = deliveryCoordinates,
        items = items,
        status = status,
        deliveryDroneIds = deliveryDroneIds
    )
}

fun DeliveryOrderMongo.toDomain(): DeliveryOrder{
    return DeliveryOrder(
        id = id.toHexString(),
        customerName = customerName,
        deliveryAddress = deliveryAddress,
        deliveryCoordinates = deliveryCoordinates,
        items = items,
        status = status,
        deliveryDroneIds = deliveryDroneIds
    )
}
