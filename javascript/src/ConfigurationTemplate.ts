import {Property} from "./Property";
import {NoValueSupplied} from "./Errors";
import {Configuration} from "./Configuration";

export class ConfigurationTemplate {
    readonly settings: Map<Property<any>, () => string>;

    constructor(settings: Map<Property<any>, () => string> = new Map()) {
        this.settings = settings;
    }

    public withProp<T>(prop: Property<T>, value: T): ConfigurationTemplate {
        return new ConfigurationTemplate(
            add(this.settings, prop, () => prop.serialize(value))
        )
    }

    public requiring<T>(prop: Property<T>): ConfigurationTemplate {
        return new ConfigurationTemplate(
            add(this.settings, prop, () => {
                throw NoValueSupplied(prop.name)
            })
        )
    }

    public reify(): Configuration {
        const map = new Map<any, any>();
        this.settings.forEach((_, key) => {
            map.set(key, this.reifiedValueFor(key))
        });
        return new Configuration(map)
    }

    private reifiedValueFor(prop: Property<any>) {
        const fromEnv = process.env[prop.name];
        if (fromEnv !== undefined) {
            return fromEnv;
        }

        const serialize = this.settings.get(prop);
        if (serialize !== undefined) {
            return serialize();
        }

        throw "Invalid State"
    }
}


function add<U, V>(map: Map<U, V>, key: U, value: V): Map<U, V> {
    const newMap = new Map(map);
    newMap.set(key, value);
    return newMap;
}

