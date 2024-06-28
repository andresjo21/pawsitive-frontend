package cr.una.pawsitive.service

object ServiceManager {
    private val services = mutableMapOf<Class<*>, Any>()

    fun <T : Any> getService(serviceClass: Class<T>): T {
        if (!services.containsKey(serviceClass)) {
            val service = ServiceBuilder.buildService(serviceClass)
            services[serviceClass] = service
        }

        return services[serviceClass] as T
    }

    fun updateBaseUrl(newBaseUrl: String) {
        ServiceBuilder.updateBaseUrl(newBaseUrl)

        // Recreate all services with the new base URL
        val serviceClasses = services.keys.toList()
        services.clear()

        for (serviceClass in serviceClasses) {
            val service = ServiceBuilder.buildService(serviceClass)
            services[serviceClass] = service
        }
    }
}