package io.github.daviddenton.configur8

import io.github.daviddenton.configur8.Configuration.ConfigurationBuilder
import io.github.daviddenton.configur8.Property._
import org.scalatest.{FunSpec, ShouldMatchers}

class ConfigurationTest extends FunSpec with ShouldMatchers {

  private val intProperty = integer("anInteger")

  private case class UserType(value: String) {
    override def toString: String = value
  }

  describe("Configuration") {

    describe("building") {
      it("blows up when required value is not supplied") {
        intercept[Misconfiguration] {
          ConfigurationBuilder().requiring(intProperty).build
        }
      }

      it("successful when all values supplied") {
        ConfigurationBuilder().requiring(intProperty).withProp(intProperty, 1).build
      }
    }

    describe("retrieving value") {
      it("blows up when attempting to get an unknown property") {
        intercept[Misconfiguration] {
          ConfigurationBuilder().build.valueOf(string("unknown"))
        }
      }

      it("can define properties with user defined type") {
        val userTypeProperty = Property.of("userDefined", UserType)
        ConfigurationBuilder().withProp(userTypeProperty, UserType("bob")).build.valueOf(userTypeProperty) should be === UserType("bob")
      }

      it("uses default value") {
        ConfigurationBuilder().withProp(intProperty, 999).build.valueOf(intProperty) should be === 999
      }

      it("uses environment value in preference to system property") {
        val systemProperty = string("USER")
        ConfigurationBuilder().withProp(systemProperty, "default").build.valueOf(systemProperty) should be === System.getenv(systemProperty.name)
      }

      it("uses property value in preference to the default") {
        val property = string("A_PROP")
        System.setProperty(property.name, "value")
        ConfigurationBuilder().withProp(property, "default").build.valueOf(property) should be === System.getProperty(property.name)
      }
    }
  }
}
