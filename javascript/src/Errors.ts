export const UnknownKey = (key: string) => Error("Unknown configuration key '" + key + "'");
export const NoValueSupplied = (key: string) => Error("No value supplied for required key '" + key + "'");
