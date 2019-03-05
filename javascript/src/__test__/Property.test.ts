import {Property} from "../Property";
import {ConfigurationTemplate} from "../ConfigurationTemplate";

describe('Property', function () {
    it("correctly deserializes integer", () => {
        const intProperty = Property.int("FOO");
        process.env.FOO = "1";
        const configuration = new ConfigurationTemplate().requiring(intProperty).reify();

        expect(configuration.get(intProperty)).toBe(1)
    });

    it("correctly deserializes float", () => {
        const floatProperty = Property.float("FOO");
        process.env.FOO = "1.24";
        const configuration = new ConfigurationTemplate().requiring(floatProperty).reify();

        expect(configuration.get(floatProperty)).toBe(1.24)
    });

    it("correctly deserializes character", () => {
        const charProperty = Property.character("FOO");
        process.env.FOO = "abc";
        const configuration = new ConfigurationTemplate().requiring(charProperty).reify();

        expect(configuration.get(charProperty)).toBe("a")
    });
});
