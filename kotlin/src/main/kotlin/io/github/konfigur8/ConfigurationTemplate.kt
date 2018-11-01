package io.github.konfigur8

import java.lang.System.getProperty
import java.lang.System.getenv


/**
 * Unreified, immutable template for a Configuration object.
 */
data class ConfigurationTemplate(val settings: Map<Property<*>, () -> String> = mapOf()) {
    /**
     * Set a default or overridden property value to use in the reified Configuration
     * @param prop definition
     * *
     * @param value typed value to use
     * *
     * @param  type
     * *
     * @return A copy of the template with the new property value set
     */
    fun <T> withProp(prop: Property<T>, value: T) = copy(settings = settings + (prop to { prop.serialize(value) }))

    /**
     * Set an abstract property that needs to be overridden by another caller before the template can be reified
     * @param prop definition
     * *
     * @param  type
     * *
     * @return A copy of the template with the new abstract property
     */
    fun <T> requiring(prop: Property<T>) =
            copy(settings = settings + (prop to { throw Misconfiguration("No value supplied for key '" + prop.name + "'") }))

    /**
     * Convert the template into a concrete Configuration object. If any values are missing, a Misconfiguration will be thrown
     * @return The complete Configuration object
     */
    fun reify(): Configuration = Configuration(settings.map { it.key to reifiedValueFor(it.key) }.toMap())

    /**
     * Convert the template into a concrete Configuration object from the given map instead of the environment. If any
     * values are missing, a Misconfiguration will be thrown
     * @return The complete Configuration object
     */
    fun reifyFrom(map: Map<String, String>) = Configuration(settings.map { it.key to map.getReifiedValueFor(it.key) }.toMap())

    private fun reifiedValueFor(property: Property<*>): String {
        val sysPropValue = getProperty(property.name)
        val envValue = getenv(property.name)
        return sysPropValue ?: envValue ?: settings[property]?.invoke() ?: throw Misconfiguration("No value supplied for key '" + property.name + "'")
    }

    private fun Map<String, String>.getReifiedValueFor(property: Property<*>): String {
        return this[property.name] ?: settings[property]?.invoke() ?: throw Misconfiguration("No value supplied for key '" + property.name + "'")
    }

}
