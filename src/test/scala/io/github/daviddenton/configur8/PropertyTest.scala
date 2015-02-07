package io.github.daviddenton.configur8

import org.scalatest.{FunSpec, ShouldMatchers}

class PropertyTest extends FunSpec with ShouldMatchers {

  case class Standard(value: Int) {
    override def toString: String = value.toString
  }

  case class Doubling(value: Int)

  describe("Property") {

    it("default round-tripping is possible") {
      val customProp = Property.of("name", i => Standard(Integer.parseInt(i)))
      customProp.deserialize(customProp.serialize(Standard(99))) should be === Standard(99)
    }

    it("custom round-tripping is possible") {
      val customProp = Property.of("name", i => Doubling(Integer.parseInt(i) / 2), (t: Doubling) => (t.value * 2).toString)
      customProp.deserialize(customProp.serialize(Doubling(99))) should be === Doubling(99)
    }
  }
}
