package io.github.konfigur8

class Configuration internal constructor(private val settings: Map<Property<*>, String>) {

    /**
     * Retrieve a property value which has been deserialized from it's string form
     * @param prop the property to retrieve the value for
     * @param  type
     * @return value of the property
     */
    fun <T> valueOf(prop: Property<T>): T {
        if (!settings.containsKey(prop)) {
            throw Misconfiguration("Unknown configuration key '" + prop.name + "'")
        }
        return prop.deserialize(settings[prop].orEmpty())
    }

    /**
     * Public representation of the the settings
     * @return settings described as a map with private values hidden
     */
    fun settings() = settings.map { it.key.name to it.key.exposeMode.display(it.value) }.toMap()
}
