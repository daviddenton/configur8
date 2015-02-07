organization := "io.github.daviddenton"

name := "configur8"

description := "Typesafe(!) configuration"

version := "0.0.2"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.11.4", "2.10.5")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"

licenses +=("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

pomExtra := <scm>
  <url>git@github.com:daviddenton/configur8.git</url>
  <connection>scm:git:git@github.com:daviddenton/configur8.git</connection>
</scm>

seq(bintraySettings: _*)
