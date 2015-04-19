package io.github.configur8;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.github.configur8.Configuration.ConfigurationTemplate.configurationTemplate;
import static io.github.configur8.Property.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ConfigurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void
    returnsDefaultValueOfKeyIfNoneInTheEnvironment() {
        Configuration configuration = configurationTemplate().withProp(string("NOT_IN_ENV"), "The value").reify();
        assertThat(configuration.settings(), hasEntry("NOT_IN_ENV", "The value"));
        assertThat(configuration.valueOf(string("NOT_IN_ENV")), equalTo("The value"));
    }

    @Test
    public void
    prioritisesEnvironmentValueOverDefault() {
        Configuration configuration = configurationTemplate().withProp(string("HOME"), "a default").reify();
        assertThat(configuration.settings(), hasEntry(equalTo("HOME"), not(equalTo("a default"))));
        assertThat(configuration.valueOf(string("HOME")), not(equalTo("a default")));
    }

    @Test
    public void
    throwsIfNoEnvironmentValueOrDefaultForKey() {
        Configuration configuration = configurationTemplate().reify();
        assertThat(configuration.settings(), not(hasKey("MISSING")));

        thrown.expectMessage(containsString("MISSING"));
        thrown.expect(Misconfiguration.class);

        configuration.valueOf(string("MISSING"));
    }

    @Test
    public void
    throwsIfRequiredOverrideIsNotSuppliedWhenCallingReify() {
        Property<String> propertyThatNeedsOverriding = string("REQUIRED_OVERRRIDE");

        thrown.expect(Misconfiguration.class);

        configurationTemplate().requiring(propertyThatNeedsOverriding).reify();
    }
}
