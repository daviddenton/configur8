package io.github.configur8;

/**
 * A typed property which can be serialized and deserialized to a string value
 */
public class Property<T> {

    public final String name;
    public final Deserializer<T> deserialize;
    public final Serializer<T> serialize;
    public final ExposeMode exposeMode;

    private Property(String name, Deserializer<T> deserialize, Serializer<T> serialize, ExposeMode exposeMode) {
        this.name = name;
        this.deserialize = deserialize;
        this.serialize = serialize;
        this.exposeMode = exposeMode;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Create a Property
     * @param name of the property
     * @param deserialize function
     * @param serialize function
     * @return The property
     */
    public static <T> Property<T> of(String name, Deserializer<T> deserialize, Serializer<T> serialize) {
        return of(name, deserialize, serialize, ExposeMode.Public);
    }

    /**
     * Create a Property
     * @param name of the property
     * @param deserialize function
     * @return The property
     */
    public static <T> Property<T> of(String name, Deserializer<T> deserialize) {
        return of(name, deserialize, Object::toString, ExposeMode.Public);
    }

    /**
     * Create a Property
     * @param name of the property
     * @param deserialize function
     * @param expose mode which determines if the value should be publicly accessible or concealed
     * @return The property
     */
    public static <T> Property<T> of(String name, Deserializer<T> deserialize, ExposeMode exposeMode) {
        return of(name, deserialize, Object::toString, exposeMode);
    }


    /**
     * Create a Property
     * @param name of the property
     * @param deserialize function
     * @param serialize function
     * @param expose mode which determines if the value should be publicly accessible or concealed
     * @return The property
     */
    public static <T> Property<T> of(String name, Deserializer<T> deserialize, Serializer<T> serialize, ExposeMode exposeMode) {
        return new Property<>(name, deserialize, serialize, exposeMode);
    }

    /**
     * Pre-configured String Property
     */
    public static Property<String> string(String name) {
        return string(name, ExposeMode.Public);
    }

    /**
     * Pre-configured String Property
     */
    public static Property<String> string(String name, ExposeMode exposeMode) {
        return new Property<>(name, String::new, s -> s, exposeMode);
    }

    /**
     * Pre-configured Int Property
     */
    public static Property<Integer> integer(String name) {
        return integer(name, ExposeMode.Public);
    }

    /**
     * Pre-configured Int Property
     */
    public static Property<Integer> integer(String name, ExposeMode exposeMode) {
        return new Property<>(name, Integer::parseInt, Object::toString, exposeMode);
    }

    /**
     * Pre-configured Long Property
     */
    public static Property<Long> aLong(String name) {
        return aLong(name, ExposeMode.Public);
    }

    /**
     * Pre-configured Long Property
     */
    public static Property<Long> aLong(String name, ExposeMode exposeMode) {
        return new Property<>(name, Long::parseLong, Object::toString, exposeMode);
    }

    /**
     * Pre-configured Boolean Property
     */
    public static Property<Boolean> bool(String name) {
        return bool(name, ExposeMode.Public);
    }

    /**
     * Pre-configured Boolean Property
     */
    public static Property<Boolean> bool(String name, ExposeMode exposeMode) {
        return new Property<>(name, Boolean::parseBoolean, Object::toString, exposeMode);
    }

    /**
     * Pre-configured Char Property
     */
    public static Property<Character> character(String name) {
        return character(name, ExposeMode.Public);
    }

    /**
     * Pre-configured Char Property
     */
    public static Property<Character> character(String name, ExposeMode exposeMode) {
        return new Property<>(name, (in) -> in.charAt(0), Object::toString, exposeMode);
    }
}
