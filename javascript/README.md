# configur8js

Nano-library which provides the ability to define typesafe (!) Configuration templates for applications.


### Install
```
yarn add configur8js
```

```
npm install --save configur8js
```

## How to use

Create properties holding the name of the env variable and the desired type:
```typescript
import {Property} from "configur8js"

export const SOME_STRING_ENV = Property.string("SOME_STRING_ENV");
export const SOME_INT_ENV = Property.int("SOME_INT_ENV");
export const SOME_FLOAT_ENV = Property.float("SOME_INT_ENV");
```

Create a configuration template, specifying defaults if needed.

```typescript
import {ConfigurationTemplate} from "configur8js"

export class Settings {
    static template = new ConfigurationTemplate()
        .withProp(SOME_STRING_ENV, "This will be the default value")
        .withProp(SOME_INT_ENV, 42);
        .requiring(SOME_FLOAT_ENV) // Will throw when you forgot to set it in your environment
    
}
```

Reify the template and enjoy type safety!

```typescript
const config = Settings.template.reify();

const someString: string = config.get(SOME_STRING_ENV)
const someInt: number = config.get(SOME_INT_ENV)

```

## Extend with your own types

You can extend the property library with your own types. This way you can convert environment variable into whatever type you require.

Just provide a deserialization function to the property constructor.

```typescript
import {Property, ConfigurationTemplate} from "configur8js"

class Cat {
    constructor(catName: string) {
        //...
    }
}

const MY_CAT_PROPERTY = new Property<Cat>("CAT_NAME_VARIABLE", (envValue: string) => new Cat(envValue));

const config = new ConfigurationTemplate().requiring(MY_CAT_PROPERTY).reify();
const myCat: Cat = config.get(MY_CAT_PROPERTY); // Yay, type-safe \o/ 
``` 
