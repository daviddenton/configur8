package io.github.konfigur8

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.konfigur8.Property.Companion.int
import io.github.konfigur8.Property.Companion.string
import org.junit.Test
import kotlin.test.fail

class ConfigurationTest {

    private val userProperty = string("bob")
    private val envProperty = string("USER")

    @Test
    @Throws(Exception::class)
    fun blowUpWhenAttemptingToGetAnUnknownProperty() {
        try {
            ConfigurationTemplate().reify().valueOf(string("MISSING"))
            fail("expected exception")
        } catch(e: Misconfiguration) {
            assertThat(e.message!!, equalTo("Unknown configuration key 'MISSING'"))
        }
    }

    @Test
    @Throws(Exception::class)
    fun usesDefaultValueIfNoOverride() {
        val configuration = ConfigurationTemplate().withProp(userProperty, "bill").reify()
        assertThat(configuration.valueOf(userProperty), equalTo("bill"))
        assertThat(configuration[userProperty], equalTo("bill"))
    }

    @Test
    @Throws(Exception::class)
    fun usesEnvironmentValueInPreferenceToDefault() {
        val configuration = ConfigurationTemplate().requiring(envProperty).reify()
        assertThat(configuration.valueOf(envProperty), equalTo(System.getenv(envProperty.name)))
        assertThat(configuration[envProperty], equalTo(System.getenv(envProperty.name)))
    }

    @Test
    @Throws(Exception::class)
    fun canSerialiseAndDeserialiseDefaultValue() {
        val doubler = Property("doubler", { it.toInt() }, { (it * 2).toString() })
        val configuration = ConfigurationTemplate().withProp(doubler, 100).reify()
        assertThat(configuration.valueOf(doubler), equalTo(200))
        assertThat(configuration[doubler], equalTo(200))
    }

    @Test
    @Throws(Exception::class)
    fun usesSystemPropertyValueInPreferenceToAEnvironmentValue() {
        System.setProperty(envProperty.name, "NOTTHEENVUSER")
        val configuration = ConfigurationTemplate().requiring(envProperty).reify()
        assertThat(configuration.valueOf(envProperty), equalTo("NOTTHEENVUSER"))
        assertThat(configuration[envProperty], equalTo("NOTTHEENVUSER"))
    }

    @Test
    fun throwsIfNoValueIsSuppliedAtAll() {
        try {
            ConfigurationTemplate().requiring(userProperty).reify()
            fail("expected exception")
        } catch(e: Misconfiguration) {
            assertThat(e.message!!, equalTo("No value supplied for key 'bob'"))
        }
    }

    @Test
    @Throws(Exception::class)
    fun exposesMapOfPropertiesUsingExposeMode() {
        val privateProperty = string("hello", ExposeMode.Private)
        val settings = ConfigurationTemplate().withProp(privateProperty, "VALUE").reify().settings()
        assertThat(settings, equalTo(mapOf(privateProperty.name to "***********")))
    }

    @Test
    fun iteratesOverStringProperties() {
        val privateProperty = string("private password", ExposeMode.Private)
        val intProperty = int("number of things")
        val properties = ConfigurationTemplate().withProp(privateProperty, "shhhh").withProp(intProperty,123).reify()

        val list: List<Property<*>> = properties.toList()
        val expected: List<Property<*>> = listOf(privateProperty, intProperty)
        assertThat(list,equalTo(expected))
    }

}
