package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.model.DeliveryOrder

@Repository
class DeliveryOrderRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): GenericRepository<DeliveryOrder>(reactiveMongoTemplate, DeliveryOrder::class.java)
