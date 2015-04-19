package io.github.configur8;

public interface Serialiser<T> {
    String serialise(T value);
}
