package io.github.daviddenton.configur8

import io.github.daviddenton.configur8.Misconfiguration.throwUp

case class Configuration protected[configur8](settings: Map[String, String]) {
  def valueOf[T](prop: Property[T]): T = prop.deserialize.apply(settings.getOrElse(prop.name, throwUp("Unknown configuration key", prop.name).apply()))
}


