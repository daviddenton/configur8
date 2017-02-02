package io.github.configur8;

import static io.github.configur8.ConfigurationTemplate.configurationTemplate;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigurationTemplateTest {

    public static final Property<String> FOO = Property.string("FOO");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void requiringPropsIsImmutable() {
        ConfigurationTemplate originalConfig = configurationTemplate().requiring(FOO);
        ConfigurationTemplate updatedConfig = originalConfig.withProp(FOO, "bar");

        assertThat(updatedConfig.reify().valueOf(FOO), is("bar"));

        thrown.expectMessage(containsString("No value supplied for key 'FOO'"));
        thrown.expect(Misconfiguration.class);
        assertThat(originalConfig.reify().valueOf(FOO), is("foo"));
    }

    @Test
    public void overridingPropsIsImmutable() {
        ConfigurationTemplate originalConfig = configurationTemplate().withProp(FOO, "foo");
        ConfigurationTemplate updatedConfig = originalConfig.withProp(FOO, "bar");

        assertThat(originalConfig.reify().valueOf(FOO), is("foo"));
        assertThat(updatedConfig.reify().valueOf(FOO), is("bar"));
    }
}