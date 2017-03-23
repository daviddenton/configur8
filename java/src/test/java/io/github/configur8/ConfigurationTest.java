package io.github.configur8;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.github.configur8.ConfigurationTemplate.configurationTemplate;
import static io.github.configur8.Property.string;
import static java.util.Collections.singletonMap;
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
    public void usesEnvironmentValueInPreferenceToDefault() throws Exception {
        Configuration configuration = configurationTemplate().requiring(envProperty).reify();
        assertThat(configuration.valueOf(envProperty), equalTo(System.getenv(envProperty.name)));
    }

    @Test
    public void canSerialiseAndDeserialiseDefaultValue() throws Exception {
        Property<Integer> doubler = Property.of("doubler", Integer::parseInt, new Serializer<Integer>() {
            @Override
            public String serialize(Integer value) {
                return String.valueOf(value + value);
            }
        });
        Configuration configuration = configurationTemplate().withProp(doubler, 100).reify();
        assertThat(configuration.valueOf(doubler), equalTo(200));
    }

    @Test
    public void usesSystemPropertyValueInPreferenceToAnEnvironmentValue() throws Exception {
        System.setProperty(envProperty.name, "NOTTHEENVUSER");
        Configuration configuration = configurationTemplate().requiring(envProperty).reify();
        assertThat(configuration.valueOf(envProperty), equalTo("NOTTHEENVUSER"));
    }

    @Test
    public void throwsIfNoValueIsSuppliedAtAll() {
        thrown.expect(Misconfiguration.class);
        configurationTemplate().requiring(userProperty).reify();
    }

    @Test
    public void exposesMapOfPropertiesUsingExposeMode() throws Exception {
        Property<String> privateProperty = Property.string("hello", ExposeMode.Private);
        assertThat(configurationTemplate()
                .withProp(privateProperty, "VALUE")
                .reify().settings(), equalTo(singletonMap(privateProperty.name, "*********")));
    }
}
