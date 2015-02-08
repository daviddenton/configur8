package io.github.daviddenton.configur8

import org.scalatest.{FunSpec, ShouldMatchers}

class PropertyTest extends FunSpec with ShouldMatchers {

  case class Standard(value: Int) {
    override def toString: String = value.toString
  }

  case class Doubling(value: Int)

  describe("Defined property of type") {
    itAdheresToStandardTests("default", Property.of("name", i => Standard(Integer.parseInt(i))), Standard(99))
    itAdheresToStandardTests("custom", Property.of("name", i => Doubling(Integer.parseInt(i) / 2), (t: Doubling) => (t.value * 2).toString), Doubling(99))
    itAdheresToStandardTests("boolean", Property.boolean("name"), true)
    itAdheresToStandardTests("string", Property.string("name"), "testValue")
    itAdheresToStandardTests("char", Property.char("name"), 'C')
    itAdheresToStandardTests("integer", Property.integer("name"), Int.MaxValue)
    itAdheresToStandardTests("long", Property.long("name"), Long.MaxValue)
  }


  private def itAdheresToStandardTests[T](name: String, prop: Property[T], testValue: T) = {
    describe(name) {
      it("round-tripping to string and back supported") {
        prop.deserialize(prop.serialize(testValue)) should be === testValue
      }
      it("toString is just the name") {
        prop.toString should be === "name"
      }
    }
  }
}
