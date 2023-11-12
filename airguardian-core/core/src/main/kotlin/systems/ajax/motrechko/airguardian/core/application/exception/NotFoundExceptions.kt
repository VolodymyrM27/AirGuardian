package systems.ajax.motrechko.airguardian.core.application.exception

class DroneNotFoundException(message: String) : Exception(message)

class DeliveryOrderNotFoundException (message: String) : Exception(message)

class DroneIsNotAvailableException(message: String) : Exception(message)

class MonitoringObjectNotFoundException(message: String) : Exception(message)
