package io.github.configur8

import java.lang.Integer._

import io.github.configur8.Property._
import org.scalatest.{FunSpec, ShouldMatchers}

class ConfigurationTest extends FunSpec with ShouldMatchers {

  private val intProperty = integer("anInteger")
  private val stringProperty = string("aString")

  case class SimpleWrapperType(value: String) {
    override def toString: String = value
  }

  case class CustomSerializedType(value: Int) {
    def describe = "!!!" + value + "!!!"
  }

  object CustomSerializedType {
    def apply(value: String): CustomSerializedType = CustomSerializedType(parseInt(value.replace("!", "")))
  }

  describe("Configuration") {

    describe("retrieving value") {
      it("blows up when attempting to get an unknown property") {
        intercept[Misconfiguration] {
          ConfigurationTemplate().reify().valueOf(string("unknown"))
        }
      }

      it("uses default value") {
        ConfigurationTemplate().withProp(intProperty, 999).reify().valueOf(intProperty) should be === 999
      }

      it("uses environment value in preference to system property") {
        val systemProperty = string("USER")
        ConfigurationTemplate().withProp(systemProperty, "default").reify().valueOf(systemProperty) should be === System.getenv(systemProperty.name)
      }

      it("uses property value in preference to the default") {
        val property = string("A_PROP")
        System.setProperty(property.name, "value")
        ConfigurationTemplate().withProp(property, "default").reify().valueOf(property) should be === System.getProperty(property.name)
      }

      it("can define properties with user defined type") {
        val property = Property.of("simpleWrapper", SimpleWrapperType)
        ConfigurationTemplate().withProp(property, SimpleWrapperType("bob")).reify().valueOf(property) should be === SimpleWrapperType("bob")
      }

      it("can define properties with a type that uses custom serialization and deserialization") {
        val property = Property.of[CustomSerializedType]("customSerialize", CustomSerializedType(_), c => c.describe)
        ConfigurationTemplate().withProp(property, CustomSerializedType("!!!5!!!")).reify().valueOf(property) should be === CustomSerializedType("!!!5!!!")
      }
    }

    it("exposes a map of the properties") {
      val settings = ConfigurationTemplate().withProp(intProperty, 1).withProp(stringProperty, "42").reify().settings
      settings should be === Map(intProperty.name -> "1", stringProperty.name -> "42")
    }
  }
}
