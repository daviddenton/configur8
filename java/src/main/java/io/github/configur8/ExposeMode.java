package io.github.configur8;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Determines how a property should be displayed
 */
public interface ExposeMode {

    /**
     * Displays input value. Use for public property values
     */
    ExposeMode Public = new ExposeMode() {
        @Override
        public String display(String value) {
            return value;
        }
    };

    /**
     * Conceals value with '*' characters. Use for Passwords and other sensitive values
     */
    ExposeMode Private = new ExposeMode() {
        @Override
        public String display(String value) {
            return IntStream.range(0, value.length()).mapToObj((c) -> "*").collect(Collectors.joining("*"));
        }
    };

    /**
     * converts a value to how it should be exposed publicly
     *
     * @param value the input value
     * @return the displayable value
     */
    String display(String value);
}
