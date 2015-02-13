package io.github.configur8

import org.scalatest.{FunSpec, ShouldMatchers}

class ExposeModeTest extends FunSpec with ShouldMatchers {

  describe("Expose mode") {
    it("Public") {
      ExposeMode.Public.display("input") should be === "input"
    }

    it("Private conceals the value with '*' characters") {
      ExposeMode.Private.display("input") should be === "*********"
    }
  }
}
