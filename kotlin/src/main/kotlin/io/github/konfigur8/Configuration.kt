package io.github.konfigur8

class Configuration internal constructor(private val settings: Map<Property<*>, String>) : Iterable<Property<*>> {

    /**
     * Retrieve a property value which has been deserialized from it's string form
     * @param prop the property to retrieve the value for
     * @return value of the property
     */
    fun <T> valueOf(prop: Property<T>) = prop.deserialize(
            settings.getOrElse(prop, { throw Misconfiguration("Unknown configuration key '" + prop.name + "'") })
    )

    /**
     * Retrieve a property value which has been deserialized from it's string form
     * @param prop the property to retrieve the value for
     * @return value of the property
     */
    operator fun <T> get(prop: Property<T>) = prop.deserialize(
            settings.getOrElse(prop, { throw Misconfiguration("Unknown configuration key '" + prop.name + "'") })
    )

    /**
     * Public representation of the the settings
     * @return settings described as a map with private values hidden
     */
    fun settings() = settings.map { it.key.name to it.key.exposeMode.display(it.value) }.toMap()

    /**
     * allows you to iterate over property keys
     * @return property key iterator
     */
    override fun iterator(): Iterator<Property<*>> = settings.keys.iterator()

}
