package io.github.daviddenton.configur8

case class Property[T] private(name: String, deserialize: (String => T), serialize: (T => String))

object Property {
  def of[T](name: String, deserialize: String => T, serialize: (T => String) = (i: T) => i.toString): Property[T] = Property[T](name, deserialize, serialize)

  def string(name: String): Property[String] = Property.of(name, (i: String) => i)

  def integer(name: String): Property[Int] = Property.of(name, (i: String) => i.toInt)
}
