package io.github.daviddenton.configur8

import io.github.daviddenton.configur8.Misconfiguration.throwUp

/**
 * A Configuration object is a successfully reified container for all of the properties that were defined.
 */
case class Configuration protected[configur8](settings: Map[String, String]) {

  /**
   * Retrieve a property value which has been deserialized from it's string form
   * @param Property definition to retrieve
   * @tparam Type of the parameter
   * @return The deserialized parameter as an instance of the correct type
   */
  def valueOf[T](prop: Property[T]): T = prop.deserialize.apply(settings.getOrElse(prop.name, throwUp("Unknown configuration key", prop.name).apply()))
}


