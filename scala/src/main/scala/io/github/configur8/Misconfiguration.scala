package io.github.configur8

/**
 * Thrown when a problem occurs in creating a Configuration or retrieving a Property
 */
class Misconfiguration(message: String) extends RuntimeException(message)

object Misconfiguration {
  def throwUp(message: String, name: String) = () => throw new Misconfiguration(message + " '" + name + "'")
}