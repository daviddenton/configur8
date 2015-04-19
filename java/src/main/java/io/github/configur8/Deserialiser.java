package io.github.configur8;

public interface Deserialiser<T> {
    T deserialise(String value);
}
