package io.github.configur8;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.System.getenv;
import static java.util.Collections.unmodifiableMap;
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
            return new Configuration(
                unmodifiableMap(settings.keySet().stream().collect(toMap(
                    key -> key,
                    key -> ofNullable(getenv(key)).<Supplier<String>>map(v -> () -> v).orElse(settings.get(key)).get(),
                    (key, value) -> value,
                    LinkedHashMap::new))));
        }
    }

}
