import {ExposeMode} from "./ExposeMode";

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
