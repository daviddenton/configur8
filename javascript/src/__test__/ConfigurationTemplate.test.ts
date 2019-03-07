import {ConfigurationTemplate, NoValueSupplied, Property} from "../index";

describe("ConfigurationTemplate", () => {
    it("is immutable", () => {
        const FOO = Property.string("FOO");

        const originalConfig = new ConfigurationTemplate().withProp(FOO, "foo");
        const updatedConfig = originalConfig.withProp(FOO, "bar");

        expect(originalConfig.reify().get(FOO)).toEqual("foo");
        expect(updatedConfig.reify().get(FOO)).toEqual("bar");
    });

    it("throws an exception when requiring and no value is given", () => {
        const FOO = Property.string("FOO");

        const configurationTemplate = new ConfigurationTemplate().requiring(FOO);
        expect(() => configurationTemplate.reify()).toThrow(NoValueSupplied("FOO"))
    });

    it("gives env variable precedence over default", () => {
        const FOO = Property.string("FOO");
        process.env.FOO = "from env";

        const configurationTemplate = new ConfigurationTemplate().withProp(FOO, "default");

        expect(configurationTemplate.reify().get(FOO)).toEqual("from env")
    });

    it("uses default when env variable is undefined", () => {
        const FOO = Property.string("FOO");
        delete process.env.FOO;
        const configurationTemplate = new ConfigurationTemplate().withProp(FOO, "default");

        expect(configurationTemplate.reify().get(FOO)).toEqual("default")
    });

});
