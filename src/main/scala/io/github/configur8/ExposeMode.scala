package io.github.configur8


/**
 * Determines how a property should be displayed
 */
trait ExposeMode {
  /**
   * converts a value to how it should be exposed publicly
   * @param value the input value
   * @return the displayable value
   */
  def display(value: String): String
}

object ExposeMode {
  /**
   * Conceals value with '*' characters. Use for Passwords and other sensitive values
   */
  val Private = new ExposeMode {
    override def display(value: String): String = (1 to value.length).map(_ => "*").mkString("*")
  }

  /**
   * Displays input value. Use for public property values
   */
  val Public = new ExposeMode {
    override def display(value: String): String = value
  }
}
