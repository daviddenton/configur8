Configur8 [![Build Status](https://travis-ci.org/daviddenton/configur8.svg)](https://travis-ci.org/daviddenton/configur8) [![Coverage Status](https://coveralls.io/repos/daviddenton/configur8/badge.svg?branch=master)](https://coveralls.io/r/daviddenton/configur8?branch=master) [![Download](https://api.bintray.com/packages/daviddenton/maven/configur8/images/download.svg) ](https://bintray.com/daviddenton/maven/configur8/_latestVersion) [ ![Watch](https://www.bintray.com/docs/images/bintray_badge_color.png) ](https://bintray.com/daviddenton/maven/configur8/view?source=watch)
=========

Nano-library which provides the ability to define typesafe (!) Configuration templates for applications.

###Concept:
A ```Configuration``` is a set of named and typed ```Property``` instances, which are defined using a  ```ConfigurationTemplate```.
Each defined Property can be set with a default value, or be blank with a requirement to be overridden. At runtime, the template is ```reified``` into a concrete ```Configuration``` object, but if any properties are missing this process will throw a ```Misconfiguration``` error.

Assuming that the reification process is successful, property values can be retrieved in an (actually) type-safe manner, and are applied in the following descending order of precedence:

1. Named environment property
2. JVM system property
3. Default value

###Get it:
Add the following dependency to ```build.sbt```:
```scala
libraryDependencies += "io.github.daviddenton" %% "configur8" % "1.1.0"
```

###See it:
See the [example code](https://github.com/daviddenton/configur8/tree/master/scala/src/test/scala/examples).
