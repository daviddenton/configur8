package io.github.configur8

import io.github.configur8.ExposeMode.Public

/**
 * A typed property which can be serialized and deserialized to a string value
 */
case class Property[T] private(name: String, deserialize: (String => T), serialize: (T => String), exposeMode: ExposeMode) {
  override def toString = name
}

object Property {
  /**
   * Create a Property
   * @param name of the property
   * @param deserialize function
   * @param serialize function
   * @param expose mode which determines if the value should be publicly accessible or concealed
   * @return The property
   */
  def of[T](name: String, deserialize: String => T, serialize: (T => String) = (i: T) => i.toString, exposeMode: ExposeMode = Public): Property[T] = Property[T](name, deserialize, serialize, exposeMode)

  /**
   * Pre-configured Char Property
   */
  def char(name: String, exposeMode: ExposeMode = Public): Property[Char] = Property.of(name, (i: String) => i.charAt(0), exposeMode = exposeMode)

  /**
   * Pre-configured String Property
   */
  def string(name: String, exposeMode: ExposeMode = Public): Property[String] = Property.of(name, (i: String) => i, exposeMode = exposeMode)

  /**
   * Pre-configured Integer Property
   */
  def integer(name: String, exposeMode: ExposeMode = Public): Property[Int] = Property.of(name, (i: String) => i.toInt, exposeMode = exposeMode)

  /**
   * Pre-configured Long Property
   */
  def long(name: String, exposeMode: ExposeMode = Public): Property[Long] = Property.of(name, (i: String) => i.toLong, exposeMode = exposeMode)

  /**
   * Pre-configured boolean Property
   */
  def boolean(name: String, exposeMode: ExposeMode = Public): Property[Boolean] = Property.of(name, (i: String) => i.toBoolean, exposeMode = exposeMode)
}
