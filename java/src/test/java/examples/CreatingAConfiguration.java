package examples;

import io.github.configur8.Configuration;
import io.github.configur8.ConfigurationTemplate;
import io.github.configur8.ExposeMode;
import io.github.configur8.Property;

import java.util.function.Supplier;

import static examples.Title.title;
import static io.github.configur8.ConfigurationTemplate.configurationTemplate;

public class CreatingAConfiguration {

    public static void main(String[] args) {
        Property<String> USER = Property.string("USER");
        Property<Integer> AGE = Property.integer("AGE");
        Property<Title> TITLE = Property.of("TITLE", Title::title);
        Property<String> PASSWORD = Property.string("PASSWORD", ExposeMode.Private);
        Property<String> RUNTIME = Property.string("java.runtime.version");
        Property<Duration> PATIENCE_LEVEL = Property.of("DURATION", Duration::duration, Duration::describe);
        Property<String> UNKNOWN = Property.string("UNKNOWN");

        ConfigurationTemplate configTemplate = configurationTemplate()
                .requiring(USER) // will be supplied by the environment
                .requiring(RUNTIME) // will be supplied by the VM
                .withProp(AGE, 2) // falls back to a default value
                .withProp(PASSWORD, "my_secret_value") // falls back to a default value
                .requiring(TITLE) // no value - requires overriding
                .withProp(PATIENCE_LEVEL, Duration.duration(10)); // custom type property with default

        System.out.println("Attempt to build an incomplete config: " + tryIt(configTemplate::reify));

        Configuration config = configTemplate.withProp(TITLE, title("Dr")).reify();

        System.out.println("Attempt to get 'UNKNOWN' property: " + tryIt(() -> config.valueOf(UNKNOWN)));
        System.out.println("The 'TITLE' supplied by the user is: " + config.valueOf(TITLE));
        System.out.println("The 'USER' supplied by the environment is: " + config.valueOf(USER));
        System.out.println("The 'RUNTIME' supplied by the System is: " + config.valueOf(USER));
        System.out.println("The 'AGE' fell back to the default value of: " + config.valueOf(AGE));
        System.out.println("The 'PASSWORD' fell back to the default value of: " + config.valueOf(PASSWORD));
        System.out.println("Type-safe retrieval of 'PATIENCE_LEVEL': " + config.valueOf(PATIENCE_LEVEL));
        System.out.println("Publicly visible settings hides the private values: " + config.settings());
    }

    private static Object tryIt(Supplier supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            return t;
        }
    }
}
