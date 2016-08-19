package io.github.konfigur8

import java.util.*

class Property<T>(val name: String, val deserialize: (String) -> T, val serialize: (T) -> String = { it: T -> it.toString() }, val exposeMode: ExposeMode = ExposeMode.Public) {
    override fun toString() = name

    companion object {
        fun string(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { it }, exposeMode = exposeMode)

        fun int(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { it.toInt() }, exposeMode = exposeMode)

        fun long(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { it.toLong() }, exposeMode = exposeMode)

        fun bool(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { it.toBoolean() }, exposeMode = exposeMode)

        fun character(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { it.first() }, exposeMode = exposeMode)

        fun uuid(name: String, exposeMode: ExposeMode = ExposeMode.Public) = Property(name, { UUID.fromString(it) }, exposeMode = exposeMode)
    }
}