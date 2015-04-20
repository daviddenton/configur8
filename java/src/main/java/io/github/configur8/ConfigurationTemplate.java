package io.github.configur8;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

/**
 * Unreified, immutable template for a Configuration object.
 */
public class ConfigurationTemplate {
    private final Map<Property<?>, Supplier<String>> settings = new HashMap<>();

    /**
     * Set a default or overridden property value to use in the reified Configuration
     * @param Property definition
     * @param The typed value to use
     * @return A copy of the template with the new property value set
     */
    public <T> ConfigurationTemplate withProp(Property<T> prop, T value) {
        settings.put(prop, value::toString);
        return this;
    }

    /**
     * Set an abstract property that needs to be overridden by another caller before the template can be reified
     * @param Property definition
     * @return A copy of the template with the new abstract property
     */
    public <T> ConfigurationTemplate requiring(Property<T> prop) {
        settings.put(prop, () -> {
            throw new Misconfiguration("No value supplied for key '" + prop.name + "'");
        });
        return this;
    }

    /**
     * Start here...
     */
    public static ConfigurationTemplate configurationTemplate() {
        return new ConfigurationTemplate();
    }

    /**
     * Convert the template into a concrete Configuration object. If any values are missing, a Misconfiguration will be thrown
     * @return The complete Configuration object
     */
    public Configuration reify() {
        Map<Property<?>, String> reified = settings.keySet().stream().collect(toMap(
                property -> property,
                this::reifiedValueFor));
        return new Configuration(reified);
    }

    private String reifiedValueFor(Property<?> property) {
        Optional<Supplier<String>> envValue = ofNullable(getenv(property.name)).map((s) -> () -> s);
        Optional<Supplier<String>> sysPropValue = ofNullable(getProperty(property.name)).map((s) -> () -> s);
        return envValue.orElse(sysPropValue.orElse(settings.get(property))).get();
    }
}
