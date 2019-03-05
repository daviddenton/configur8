import {Property} from "./Property";
import {UnknownKey} from "./Errors";

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
