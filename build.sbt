organization := "io.github.daviddenton"

name := "configur8"

description := "Typesafe(!) configuration"

version := "0.0.6"

scalaVersion := "2.11.5"

crossScalaVersions := Seq("2.10.4", "2.11.5")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

licenses +=("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

pomExtra :=
  <url>http://daviddenton.github.io/
    {name}
  </url>
    <scm>
      <url>git@github.com:daviddenton/
        {name}
        .git</url>
      <connection>scm:git:git@github.com:daviddenton/
        {name}
        .git</connection>
      <developerConnection>scm:git:git@github.com:daviddenton/
        {name}
        .git</developerConnection>
    </scm>
    <developers>
      <developer>
        <name>David Denton</name>
        <email>mail@daviddenton.github.io</email>
        <organization>
          {organization}
        </organization>
        <organizationUrl>http://daviddenton.github.io</organizationUrl>
      </developer>
    </developers>

pomExtra += <ran/>

seq(bintraySettings: _*)
