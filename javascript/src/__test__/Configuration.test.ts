import {Property} from "../Property";
import {Configuration} from "../Configuration";
import {UnknownKey} from "../Errors";
import {ConfigurationTemplate} from "../ConfigurationTemplate";

describe("Configuration", () => {
    it("throws an exception when key is not in configuration", () => {
        const FOO = Property.string("FOO");
        const configuration = new Configuration();

        expect(() => configuration.get(FOO)).toThrow(UnknownKey("FOO"))
    });

    it("doesnt throw for falsy value 0", () => {
        const FOO = Property.int("FOO");
        process.env.FOO = "0";
        const configuration = new ConfigurationTemplate().requiring(FOO).reify();

        expect(() => configuration.get(FOO)).not.toThrow()
    })
});
