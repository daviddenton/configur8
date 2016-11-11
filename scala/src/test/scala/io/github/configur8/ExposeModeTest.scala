package io.github.configur8

import org.scalatest.{FunSpec, Matchers}

class ExposeModeTest extends FunSpec with Matchers {

  describe("Expose mode") {
    it("Public") {
      ExposeMode.Public.display("input") shouldBe "input"
    }

    it("Private conceals the value with '*' characters") {
      ExposeMode.Private.display("input") shouldBe "*********"
    }
  }
}
