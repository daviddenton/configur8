export class Configuration {
    private readonly settings: Map<Property<any>, any>;

    constructor(settings: Map<Property<any>, any> = new Map()) {
        this.settings = settings;
    }

    public get<T>(prop: Property<T>) {
        const value = this.settings.get(prop);
        if (!value) {
            throw UnknownKey(prop.name)
        }
        return prop.deserialize(value)
    }
}

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

export class Property<T> {
    public readonly name: string;
    public readonly deserialize: (value: string) => T;
    public readonly serialize: (value: T) => string;
    public readonly exposeMode: ExposeMode;

    constructor(
        name: string,
        deserialize: (value: string) => T,
        serialize: (value: T) => string = (value: T) => value.toString(),
        exposeMode: ExposeMode = ExposeMode.Public
    ) {
        this.name = name;
        this.deserialize = deserialize;
        this.serialize = serialize;
        this.exposeMode = exposeMode;
    }

    static string(name: string, exposeMode: ExposeMode = ExposeMode.Public) {
        return new Property(
            name,
            (value) => value,
            (value) => value.toString(),
            exposeMode
        )
    }

    static int(name: string, exposeMode: ExposeMode = ExposeMode.Public) {
        return new Property(
            name,
            (value) => parseInt(value),
            (value) => value.toString(),
            exposeMode
        )
    }

    static float(name: string, exposeMode: ExposeMode = ExposeMode.Public) {
        return new Property(
            name,
            (value) => parseFloat(value),
            (value) => value.toString(),
            exposeMode
        )
    }

    static character(name: string, exposeMode: ExposeMode = ExposeMode.Public) {
        return new Property(
            name,
            (value) => value.charAt(0),
            (value) => value.toString(),
            exposeMode
        )
    }
}

export enum ExposeMode {
    Public, Private
}

export const UnknownKey = (key: string) => Error("Unknown configuration key '" + key + "'");
export const NoValueSupplied = (key: string) => Error("No value supplied for required key '" + key + "'");

