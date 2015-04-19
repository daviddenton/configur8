package io.github.configur8;

/**
 * A typed property which can be serialized and deserialized to a string value
 */
public class Property<T> {

    public final String name;
    public final Deserialiser<T> deserialise;
    public final Serialiser<T> serialise;
    public final ExposeMode exposeMode;

    private Property(String name, Deserialiser<T> deserialise, Serialiser<T> serialise, ExposeMode exposeMode) {
        this.name = name;
        this.deserialise = deserialise;
        this.serialise = serialise;
        this.exposeMode = exposeMode;
    }

    @Override
    public String toString() {
        return name;
    }

    public static <T> Property<T> of(String name, Deserialiser<T> deserialise, Serialiser<T> serialise) {
        return of(name, deserialise, serialise, ExposeMode.Public);
    }

    public static <T> Property<T> of(String name, Deserialiser<T> deserialise) {
        return of(name, deserialise, Object::toString, ExposeMode.Public);
    }

    public static <T> Property<T> of(String name, Deserialiser<T> deserialise, ExposeMode exposeMode) {
        return of(name, deserialise, Object::toString, exposeMode);
    }

    public static <T> Property<T> of(String name, Deserialiser<T> deserialise, Serialiser<T> serialise, ExposeMode exposeMode) {
        return new Property<>(name, deserialise, serialise, exposeMode);
    }

    public static Property<String> string(String name) {
        return string(name, ExposeMode.Public);
    }

    public static Property<String> string(String name, ExposeMode exposeMode) {
        return new Property<>(name, String::new, s -> s, exposeMode);
    }

    public static Property<Integer> integer(String name) {
        return integer(name, ExposeMode.Public);
    }

    public static Property<Integer> integer(String name, ExposeMode exposeMode) {
        return new Property<>(name, Integer::parseInt, Object::toString, exposeMode);
    }

    public static Property<Long> aLong(String name) {
        return aLong(name, ExposeMode.Public);
    }

    public static Property<Long> aLong(String name, ExposeMode exposeMode) {
        return new Property<>(name, Long::parseLong, Object::toString, exposeMode);
    }

    public static Property<Boolean> bool(String name) {
        return bool(name, ExposeMode.Public);
    }

    public static Property<Boolean> bool(String name, ExposeMode exposeMode) {
        return new Property<>(name, Boolean::parseBoolean, Object::toString, exposeMode);
    }

    public static Property<Character> character(String name) {
        return character(name, ExposeMode.Public);
    }

    public static Property<Character> character(String name, ExposeMode exposeMode) {
        return new Property<>(name, (in) -> in.charAt(0), Object::toString, exposeMode);
    }
}
