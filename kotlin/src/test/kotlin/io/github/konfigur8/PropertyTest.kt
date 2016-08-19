package io.github.konfigur8

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class PropertyTest {

    data class Standard(val value: Int) {
        override fun toString() = value.toString()
    }

    @Test
    @Throws(Exception::class)
    fun propertiesOfType() {
        itAdheresToStandardContract("default", Standard(99), Property("name", { s -> Standard(Integer.parseInt(s)) }))
        itAdheresToStandardContract("custom serialising", Standard(99),
                Property("name", { s: String -> Standard(Integer.parseInt(s) / 2) }, { t: Standard -> (t.value * 2).toString() }))
        itAdheresToStandardContract("boolean", true, Property.bool("name"))
        itAdheresToStandardContract("string", "value", Property.string("name"))
        itAdheresToStandardContract("character", 'c', Property.character("name"))
        itAdheresToStandardContract("int", 2, Property.int("name"))
        itAdheresToStandardContract("long", 2L, Property.long("name"))
    }

    private fun <T> itAdheresToStandardContract(name: String, testValue: T, prop: Property<T>) {
        assertThat(name, prop.deserialize(prop.serialize(testValue)), equalTo(testValue))
        assertThat(name, prop.toString(), equalTo(prop.name))
    }
}