package io.github.configur8

import io.github.configur8.Misconfiguration.throwUp

/**
 * A Configuration object is a successfully reified container for all of the properties that were defined.
 */
case class Configuration protected[configur8](private val propertySettings: Map[Property[_], String]) {

  /**
   * Public representation of the the settings
   */
  lazy val settings: Map[String, String] = propertySettings.map {
    case (p, v) => {
      (p.name, p.exposeMode.display(v))
    }
  }.toMap

  /**
   * Retrieve a property value which has been deserialized from it's string form
   * @param Property definition to retrieve
   * @tparam Type of the parameter
   * @return The deserialized parameter as an instance of the correct type
   */
  def valueOf[T](prop: Property[T]): T = prop.deserialize.apply(propertySettings.getOrElse(prop, throwUp("Unknown configuration key", prop.name).apply()))
}


