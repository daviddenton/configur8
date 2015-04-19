package examples;

public class Duration {
    private final int value;

    private Duration(int value) {
        this.value = value;
    }

    public String describe() {
        return value + "s";
    }

    public static Duration duration(int seconds) {
        return new Duration(seconds);
    }
}
