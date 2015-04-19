package io.github.configur8;

public class Misconfiguration extends RuntimeException {

    public static void throwUp(String message, String name) {
        throw new Misconfiguration(message + " '" + name + "'");
    }

    public Misconfiguration(String message) {
        super(message);
    }
}
