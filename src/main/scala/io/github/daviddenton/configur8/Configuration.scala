package io.github.daviddenton.configur8

import scala.util.Properties

case class Configuration private(settings: Map[String, String]) {
  def valueOf[T](prop: Property[T]): T = prop.deserialize.apply(settings.getOrElse(prop.name, Configuration.throwUp("Unknown configuration key", prop.name).apply()))
}

object Configuration {

  private def throwUp(message: String, name: String) = () => throw new Misconfiguration(message + " '" + name + "'")

  class Misconfiguration(message: String) extends RuntimeException(message)

  case class ConfigurationBuilder private(settings: Map[String, () => String]) {

    private def defaultFor(name: String): String = settings(name)()

    private def reify(name: String): (String, String) = (name, Properties.envOrNone(name).getOrElse(Properties.propOrNone(name).getOrElse(defaultFor(name))))

    def withProp[T](prop: Property[T], value: T): ConfigurationBuilder = ConfigurationBuilder(settings + (prop.name -> value.toString))

    def requiring[T](prop: Property[T]): ConfigurationBuilder = ConfigurationBuilder(settings + (prop.name -> throwUp("No value supplied for key", prop.name)))

    def build: Configuration = Configuration(settings.keys.map(reify).toMap)
  }

  object ConfigurationBuilder {
    def apply(): ConfigurationBuilder = ConfigurationBuilder(Map[String, () => String]())
  }

}
