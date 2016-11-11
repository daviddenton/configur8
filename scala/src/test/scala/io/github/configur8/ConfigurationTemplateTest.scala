package io.github.configur8

import io.github.configur8.Property._
import org.scalatest.{FunSpec, Matchers}

class ConfigurationTemplateTest extends FunSpec with Matchers {

  private val intProperty = integer("anInteger")

  describe("Configuration Template") {
    it("reification blows up when required value is not supplied") {
      intercept[Misconfiguration] {
        ConfigurationTemplate().requiring(intProperty).reify()
      }
    }

    it("reification successful when all values supplied") {
      ConfigurationTemplate().requiring(intProperty).withProp(intProperty, 1).reify()
    }
  }
}
