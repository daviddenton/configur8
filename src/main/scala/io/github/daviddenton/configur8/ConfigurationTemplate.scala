package io.github.daviddenton.configur8

import io.github.daviddenton.configur8.Misconfiguration._

import scala.util.Properties

case class ConfigurationTemplate private(settings: Map[String, () => String]) {

  private def defaultFor(name: String): String = settings(name)()

  private def reifyName(name: String): (String, String) = (name, Properties.envOrNone(name).getOrElse(Properties.propOrNone(name).getOrElse(defaultFor(name))))

  def withProp[T](prop: Property[T], value: T): ConfigurationTemplate = ConfigurationTemplate(settings + (prop.name -> (() => prop.serialize(value))))

  def requiring[T](prop: Property[T]): ConfigurationTemplate = ConfigurationTemplate(settings + (prop.name -> throwUp("No value supplied for key", prop.name)))

  def reify(): Configuration = Configuration(settings.keys.map(reifyName).toMap)
}

object ConfigurationTemplate {
  def apply(): ConfigurationTemplate = ConfigurationTemplate(Map[String, () => String]())
}
