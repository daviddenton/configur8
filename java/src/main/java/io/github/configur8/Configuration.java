package io.github.configur8;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public class Configuration {

    private final Map<Property<?>, String> settings;

    private Configuration(Map<Property<?>, String> settings) {
        this.settings = settings;
    }

    public <T> T valueOf(Property<T> prop) {
        if (!settings.containsKey(prop)) {
            throw new Misconfiguration("Unknown configuration key '" + prop.name + "'");
        }
        return prop.deserialise.deserialise(settings.get(prop));
    }

    public Map<String, String> settings() {
        return settings.keySet().stream().collect(toMap(
                property -> property.name,
                k -> k.exposeMode.display(settings.get(k))
        ));
    }

    public static class ConfigurationTemplate {
        private final Map<Property<?>, Supplier<String>> settings = new HashMap<>();

        public <T> ConfigurationTemplate withProp(Property<T> prop, T value) {
            settings.put(prop, value::toString);
            return this;
        }

        public <T> ConfigurationTemplate requiring(Property<T> prop) {
            settings.put(prop, () -> {
                throw new Misconfiguration("No value supplied for key '" + prop.name + "'");
            });
            return this;
        }

        public static ConfigurationTemplate configurationTemplate() {
            return new ConfigurationTemplate();
        }

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

}
