package io.github.configur8;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Configuration {

    private final Map<Property<?>, String> settings;

    Configuration(Map<Property<?>, String> settings) {
        this.settings = settings;
    }

    /**
     * Retrieve a property value which has been deserialized from it's string form
     * @param prop the property to retrieve the value for
     * @param <T> type
     * @return value of the property
     */
    public <T> T valueOf(Property<T> prop) {
        if (!settings.containsKey(prop)) {
            throw new Misconfiguration("Unknown configuration key '" + prop.name + "'");
        }
        return prop.deserialize.deserialize(settings.get(prop));
    }

    /**
     * Public representation of the the settings
     * @return settings described as a map with private values hidden
     */
    public Map<String, String> settings() {
        return settings.keySet().stream().collect(toMap(
                property -> property.name,
                k -> k.exposeMode.display(settings.get(k))
        ));
    }

}
