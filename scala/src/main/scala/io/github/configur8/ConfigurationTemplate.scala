package io.github.configur8

import io.github.configur8.Misconfiguration.throwUp

import scala.util.Properties

/**
 * Unreified, immutable template for a Configuration object.
 */
case class ConfigurationTemplate private(private val settings: Map[Property[_], () => String]) {

  private def defaultFor(prop: Property[_]): String = settings(prop)()

  private def reify(prop: Property[_]): (Property[_], String) = (prop, Properties.envOrNone(prop.name).getOrElse(Properties.propOrNone(prop.name).getOrElse(defaultFor(prop))))

  /**
   * Set a default or overridden property value to use in the reified Configuration
   * @param Property definition
   * @param The typed value to use
   * @return A copy of the template with the new property value set
   */
  def withProp[T](prop: Property[T], value: T): ConfigurationTemplate = ConfigurationTemplate(settings + (prop -> (() => prop.serialize(value))))

  /**
   * Set an abstract property that needs to be overridden by another caller before the template can be reified
   * @param Property definition
   * @return A copy of the template with the new abstract property
   */
  def requiring[T](prop: Property[T]): ConfigurationTemplate = ConfigurationTemplate(settings + (prop -> throwUp("No value supplied for key", prop.name)))

  /**
   * Convert the template into a concrete Configuration object. If any values are missing, a Misconfiguration will be thrown
   * @return The complete Configuration object
   */
  def reify(): Configuration = {
    Configuration(settings.keys.map(reify).toMap)
  }
}

object ConfigurationTemplate {
  def apply(): ConfigurationTemplate = ConfigurationTemplate(Map[Property[_], () => String]())
}
