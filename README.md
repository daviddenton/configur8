Configur8 [![Build Status](https://api.travis-ci.org/daviddenton/configur8.svg)](https://travis-ci.org/daviddenton/configur8) [![Coverage Status](https://coveralls.io/repos/daviddenton/configur8/badge.svg?branch=master)](https://coveralls.io/r/daviddenton/configur8?branch=master) [![Download](https://api.bintray.com/packages/daviddenton/maven/configur8/images/download.svg) ](https://bintray.com/daviddenton/maven/configur8/_latestVersion) [ ![Watch](https://www.bintray.com/docs/images/bintray_badge_color.png) ](https://bintray.com/daviddenton/maven/configur8/view?source=watch)
=========

Nano-library which provides the ability to define typesafe (!) Configuration templates for applications.

### Concept:
A ```Configuration``` is a set of named and typed ```Property``` instances, which are defined using a  ```ConfigurationTemplate```.
Each defined Property can be set with a default value, or be blank with a requirement to be overridden. At runtime, the template is ```reified``` into a concrete ```Configuration``` object, but if any properties are missing this process will throw a ```Misconfiguration``` error.

Assuming that the reification process is successful, property values can be retrieved in an (actually) type-safe manner, and are applied in the following descending order of precedence:

1. JVM system property
2. Named environment property
3. Default value

### Quick example
This is from the Kotlin version, but the Scala and Java APIs are broadly the same:
```kotlin
// simple typed values
val USER = Property.string("USER")
val AGE = Property.int("AGE")

// provide custom mapping functions to convert from strings to domain
val PATIENCE_LEVEL = Property("DURATION", { i: String -> Duration.parse(i) }, { it.describe() })

// build your template
val configTemplate = ConfigurationTemplate()
      .requiring(USER) // will be supplied by the environment
      .withProp(AGE, 2) // falls back to a default value
      .withProp(USER, "mario") // falls back to a default value
      .withProp(PATIENCE_LEVEL, Duration(10)) // custom type property with default

// attempting to build a configuration with missing values will generated
val config = configTemplate.reify()

// retrieval of values in a typesafe way
val patience: Duration = config.valueOf[PATIENCE_LEVEL]
```

### Get it:
Currently, the library is published in Java, Kotlin and Scala versions in JCenter (and synced to Maven Central).

#### Maven:
Java:
```XML
<dependency>
  <groupId>io.github.daviddenton</groupId>
  <artifactId>configur8</artifactId>
  <version>1.7.0</version>
</dependency>
```

Kotlin:
```XML
<dependency>
  <groupId>io.github.daviddenton</groupId>
  <artifactId>konfigur8</artifactId>
  <version>1.7.0</version>
</dependency>
```

#### SBT:
```scala
libraryDependencies += "io.github.daviddenton" %% "configur8" % "1.7.0"
```

### See it:
See the example code in [scala](https://github.com/daviddenton/configur8/tree/master/scala/src/test/scala/examples) or [java](https://github.com/daviddenton/configur8/tree/master/java/src/test/java/examples) or [kotlin](https://github.com/daviddenton/configur8/tree/master/kotlin/src/test/kotlin/examples)
