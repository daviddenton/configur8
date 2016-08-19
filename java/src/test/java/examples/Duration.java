package examples;

import static java.lang.Integer.parseInt;

public class Duration {
    private final int value;

    private Duration(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    public String describe() {
        return value + "s";
    }

    public static Duration duration(String seconds) {
        return new Duration(parseInt(seconds.replace("s", "")));
    }

    public static Duration duration(int seconds) {
        return new Duration(seconds);
    }
}
