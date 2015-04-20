package io.github.configur8;

/**
 * Turns a stringified property value into a property
 */
public interface Deserializer<T> {
    T deserialize(String value);
}
