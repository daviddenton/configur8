package io.github.daviddenton.configur8

/**
 * A typed property which can be serialized and deserialized to a string value
 */
case class Property[T] private(name: String, deserialize: (String => T), serialize: (T => String)) {
  override def toString = name
}

object Property {
  /**
   * Create a Property
   * @param Name of the property
   * @param deserialize function
   * @param serialize function
   * @return The property
   */
  def of[T](name: String, deserialize: String => T, serialize: (T => String) = (i: T) => i.toString): Property[T] = Property[T](name, deserialize, serialize)

  /**
   * Pre-configured Char Property
   */
  def char(name: String): Property[Char] = Property.of(name, (i: String) => i.charAt(0))

  /**
   * Pre-configured String Property
   */
  def string(name: String): Property[String] = Property.of(name, (i: String) => i)

  /**
   * Pre-configured Integer Property
   */
  def integer(name: String): Property[Int] = Property.of(name, (i: String) => i.toInt)

  /**
   * Pre-configured Long Property
   */
  def long(name: String): Property[Long] = Property.of(name, (i: String) => i.toLong)

  /**
   * Pre-configured boolean Property
   */
  def boolean(name: String): Property[Boolean] = Property.of(name, (i: String) => i.toBoolean)
}
