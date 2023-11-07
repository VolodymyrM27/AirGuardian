package systems.ajax.motrechko.airguardian.repository

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Repository
import systems.ajax.motrechko.airguardian.model.BatteryApplication

@Repository
class BatteryApplicationRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): GenericRepository<BatteryApplication>(reactiveMongoTemplate, BatteryApplication::class.java)
