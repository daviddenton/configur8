package io.github.configur8;

public interface Serializer<T> {
    String serialize(T value);
}
