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
    private final Map<Property<?>, Supplier<String>> settings;

    private ConfigurationTemplate(Map<Property<?>, Supplier<String>> settings) {
        this.settings = settings;
    }

    /**
     * Set a default or overridden property value to use in the reified Configuration
     * @param prop definition
     * @param value typed value to use
     * @param <T> type
     * @return A copy of the template with the new property value set
     */
    public <T> ConfigurationTemplate withProp(Property<T> prop, T value) {
        return new ConfigurationTemplate(putProperty(prop, () -> prop.serialize.serialize(value)));
    }

    /**
     * Set an abstract property that needs to be overridden by another caller before the template can be reified
     * @param prop definition
     * @param <T> type
     * @return A copy of the template with the new abstract property
     */
    public <T> ConfigurationTemplate requiring(Property<T> prop) {
        return new ConfigurationTemplate(putProperty(prop, () -> {
            throw new Misconfiguration("No value supplied for key '" + prop.name + "'");
        }));
    }

    /**
     * Start here...
     * @return An empty template
     */
    public static ConfigurationTemplate configurationTemplate() {
        return new ConfigurationTemplate(new HashMap<>());
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
        Optional<Supplier<String>> sysPropValue = ofNullable(getProperty(property.name)).map((s) -> () -> s);
        Optional<Supplier<String>> envValue = ofNullable(getenv(property.name)).map((s) -> () -> s);
        return sysPropValue.orElse(envValue.orElse(settings.get(property))).get();
    }

    private <T> Map<Property<?>, Supplier<String>> putProperty(Property<T> prop, Supplier<String> stringSupplier) {
        HashMap<Property<?>, Supplier<String>> newSettings = new HashMap<>(settings);
        newSettings.put(prop, stringSupplier);
        return newSettings;
    }
}
