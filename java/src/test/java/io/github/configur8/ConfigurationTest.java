package io.github.configur8;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

import static io.github.configur8.Configuration.ConfigurationTemplate.configurationTemplate;
import static io.github.configur8.Property.string;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ConfigurationTest {


    private final Property<String> userProperty = Property.string("bob");
    private final Property<String> envProperty = string("USER");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void blowUpWhenAttemptingToGetAnUnknownProperty() throws Exception {
        Configuration configuration = configurationTemplate().reify();
        thrown.expectMessage(containsString("MISSING"));
        thrown.expect(Misconfiguration.class);
        configuration.valueOf(Property.string("MISSING"));
    }

    @Test
    public void usesDefaultValueIfNoOverride() throws Exception {
        Configuration configuration = configurationTemplate().withProp(userProperty, "bill").reify();
        assertThat(configuration.valueOf(userProperty), equalTo("bill"));
    }

    @Test
    public void usesSystemPropertyValueInPreferenceToDefault() throws Exception {
        System.setProperty(userProperty.name, "NOTTHEENVUSER");
        Configuration configuration = configurationTemplate().requiring(userProperty).reify();
        assertThat(configuration.valueOf(userProperty), equalTo(System.getProperty(userProperty.name)));
    }

    @Test
    public void usesEnvironmentValueInPreferenceToASystemProperty() throws Exception {
        System.setProperty(envProperty.name, "NOTTHEENVUSER");
        Configuration configuration = configurationTemplate().requiring(envProperty).reify();
        assertThat(configuration.valueOf(envProperty), equalTo(System.getenv(envProperty.name)));
    }

    @Test
    public void throwsIfNoValueIsSuppliedAtAll() {
        thrown.expect(Misconfiguration.class);
        configurationTemplate().requiring(userProperty).reify();
    }

    @Test
    public void exposesMapOfProperties() throws Exception {
        assertThat(configurationTemplate().withProp(userProperty, "VALUE").reify().settings(), equalTo(Collections.singletonMap(userProperty.name, "VALUE")));
    }
}
