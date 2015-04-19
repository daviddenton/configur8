package io.github.configur8;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PropertyTest {

    class Standard {
        final Integer value;

        Standard(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Standard standard = (Standard) o;

            if (value != null ? !value.equals(standard.value) : standard.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

    @Test
    public void propertiesOfType() throws Exception {
        itAdheresToStandardContract("default", new Standard(99), Property.of("name",
                        s -> new Standard(Integer.parseInt(s)))
        );
        itAdheresToStandardContract("custom serialising", new Standard(99), Property.of("name",
                (String s) -> new Standard(Integer.parseInt(s) / 2), (Standard t) -> String.valueOf(t.value * 2))
        );
        itAdheresToStandardContract("boolean", true, Property.bool("name"));
        itAdheresToStandardContract("string", "value", Property.string("name"));
        itAdheresToStandardContract("character", 'c', Property.character("name"));
        itAdheresToStandardContract("int", 2, Property.integer("name"));
        itAdheresToStandardContract("long", 2L, Property.aLong("name"));
    }

    private <T> void itAdheresToStandardContract(String name, T testValue, Property<T> prop) {
        assertThat(name, prop.deserialise.deserialise(prop.serialise.serialise(testValue)), equalTo(testValue));
        assertThat(name, prop.toString(), equalTo(prop.name));
    }
}