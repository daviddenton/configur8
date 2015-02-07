package examples

import io.github.daviddenton.configur8.Configuration.ConfigurationBuilder
import io.github.daviddenton.configur8.Property

import scala.util.Try


object CreatingAConfiguration extends App {

  val USER = Property.string("USER")
  val AGE = Property.integer("AGE")
  val TITLE = Property.of("TITLE", Title)
  val RUNTIME = Property.string("java.runtime.version")
  val PATIENCE_LEVEL = Property.of[Duration]("DURATION", Duration(_), d => d.describe)

  val configTemplate = ConfigurationBuilder()
    .requiring(USER) // supplied by the environment
    .requiring(RUNTIME) // supplied by the VM
    .withProp(AGE, 0) // defaulting
    .requiring(TITLE) // requires overriding
    .withProp(PATIENCE_LEVEL, Duration(10)) // custom type property

  println("when I attempt to build an incomplete config: " + Try(configTemplate.build))

  val config = configTemplate.withProp(TITLE, Title("Dr")).build

  println("the user I got from the environment is: " + config.valueOf(USER))
  println("the runtime I got from the system is: " + config.valueOf(RUNTIME))
  println("the default value for AGE: " + config.valueOf(AGE))
  println("type-safe retrieval of a property got a " + config.valueOf(PATIENCE_LEVEL))
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
  def apply(value: String): Duration = Duration(value.replace("s", ""))
}
