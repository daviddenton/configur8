package io.github.daviddenton.configur8

class Misconfiguration(message: String) extends RuntimeException(message)

object Misconfiguration {
  def throwUp(message: String, name: String) = () => throw new Misconfiguration(message + " '" + name + "'")
}