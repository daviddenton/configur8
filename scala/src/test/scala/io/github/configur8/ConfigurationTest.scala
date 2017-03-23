package io.github.configur8

import java.lang.Integer._

import io.github.configur8.Property._
import org.scalatest.{FunSpec, Matchers}

class ConfigurationTest extends FunSpec with Matchers {

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
        ConfigurationTemplate().withProp(intProperty, 999).reify().valueOf(intProperty) shouldBe 999
      }

      it("uses system property in preference to an environment value") {
        val envProperty = string("USER")
        System.setProperty("USER", "value")
        ConfigurationTemplate().withProp(envProperty, "default").reify().valueOf(envProperty) shouldBe "value"
      }

      it("uses environment value in preference to the default") {
        val property = string("HOME")
        ConfigurationTemplate().withProp(property, "default").reify().valueOf(property) shouldBe System.getenv(property.name)
      }

      it("can define properties with user defined type") {
        val property = Property.of("simpleWrapper", SimpleWrapperType)
        ConfigurationTemplate().withProp(property, SimpleWrapperType("bob")).reify().valueOf(property) shouldBe SimpleWrapperType("bob")
      }

      it("can define properties with a type that uses custom serialization and deserialization") {
        val property = Property.of[CustomSerializedType]("customSerialize", CustomSerializedType(_), c => c.describe)
        ConfigurationTemplate().withProp(property, CustomSerializedType("!!!5!!!")).reify().valueOf(property) shouldBe CustomSerializedType("!!!5!!!")
      }
    }

    it("exposes a map of the properties") {
      val privateStringProp = Property.string("aString", ExposeMode.Private)
      val settings = ConfigurationTemplate().withProp(intProperty, 1).withProp(privateStringProp, "42").reify().settings
      settings shouldBe Map(intProperty.name -> "1", privateStringProp.name -> "***")
    }
  }
}
