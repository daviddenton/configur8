package examples;

public class Title {
    private final String value;

    @Override
    public String toString() {
        return value;
    }

    private Title(String value) {
        this.value = value;
    }

    public static Title title(String value) {
        return new Title(value);
    }
}
