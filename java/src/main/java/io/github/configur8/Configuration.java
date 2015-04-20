package io.github.configur8;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public class Configuration {

    private final Map<String, String> settings;

    private Configuration(Map<String, String> settings) {
        this.settings = settings;
    }

    public <T> T valueOf(Property<T> prop) {
        if (!settings.containsKey(prop.name)) {
            throw new Misconfiguration("Unknown configuration key '" + prop.name + "'");
        }
        return prop.deserialise.deserialise(settings.get(prop.name));
    }

    public Map<String, String> settings() {
        return settings;
    }

    public static class ConfigurationTemplate {

        private final Map<String, Supplier<String>> settings = new HashMap<>();

        public <T> ConfigurationTemplate withProp(Property<T> prop, T value) {
            settings.put(prop.name, value::toString);
            return this;
        }

        public <T> ConfigurationTemplate requiring(Property<T> prop) {
            settings.put(prop.name, () -> {
                throw new Misconfiguration("No value supplied for key '" + prop.name + "'");
            });
            return this;
        }

        public static ConfigurationTemplate configurationTemplate() {
            return new ConfigurationTemplate();
        }

        public Configuration reify() {
            Map<String, String> reified = settings.keySet().stream().collect(toMap(
                    propertyName -> propertyName,
                    this::reifiedValueFor,
                    (propertyName, value) -> value,
                    LinkedHashMap::new));
            return new Configuration(reified);
        }

        private String reifiedValueFor(String propertyName) {
            Optional<Supplier<String>> envValue = ofNullable(getenv(propertyName)).map((s) -> () -> s);
            Optional<Supplier<String>> sysPropValue = ofNullable(getProperty(propertyName)).map((s) -> () -> s);
            return envValue.orElse(sysPropValue.orElse(settings.get(propertyName))).get();
        }
    }

}
