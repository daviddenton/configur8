package examples

import java.lang.Integer.parseInt

import io.github.daviddenton.configur8.Configuration.ConfigurationBuilder
import io.github.daviddenton.configur8.Property

import scala.util.Try


object CreatingAConfiguration extends App {

  val USER = Property.string("USER")
  val AGE = Property.integer("AGE")
  val TITLE = Property.of("TITLE", Title)
  val RUNTIME = Property.string("java.runtime.version")
  val PATIENCE_LEVEL = Property.of[Duration]("DURATION", Duration(_), d => d.describe)
  val UNKNOWN = Property.string("UNKNOWN")

  val configTemplate = ConfigurationBuilder()
    .requiring(USER) // supplied by the environment
    .requiring(RUNTIME) // supplied by the VM
    .withProp(AGE, 0) // defaulting
    .requiring(TITLE) // requires overriding
    .withProp(PATIENCE_LEVEL, Duration(10)) // custom type property

  println("Attempt to build an incomplete config: " + Try(configTemplate.build))

  val config = configTemplate.withProp(TITLE, Title("Dr")).build

  println(s"Attempt to get '$UNKNOWN' property: ${Try(config.valueOf(UNKNOWN))}")
  println(s"The '$TITLE' supplied by the user is: ${config.valueOf(TITLE)}")
  println(s"The '$USER' supplied by the environment is: ${config.valueOf(USER)}")
  println(s"The '$RUNTIME' supplied by the System is: ${config.valueOf(RUNTIME)}")
  println(s"The '$AGE' fell back to the default value of: ${config.valueOf(AGE)}")
  println(s"Type-safe retrieval of '$PATIENCE_LEVEL': ${config.valueOf(PATIENCE_LEVEL)}")
}

// simple wrapper type which self describes the wrapper
case class Title(value: String) {
  override def toString = value
}

// wrapper with requiring custom serialization/deserialization
case class Duration(seconds: Int) {
  def describe = seconds + "s"
}

object Duration {
  def apply(value: String): Duration = Duration(parseInt(value.replace("s", "")))
}
