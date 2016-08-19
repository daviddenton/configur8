package io.github.konfigur8

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
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
    }

    @Test
    @Throws(Exception::class)
    fun usesSystemPropertyValueInPreferenceToDefault() {
        val someSystemProperty = string("bilbo")
        System.setProperty(someSystemProperty.name, "NOTTHEENVUSER")
        val configuration = ConfigurationTemplate().requiring(someSystemProperty).reify()
        assertThat(configuration.valueOf(someSystemProperty), equalTo(System.getProperty(someSystemProperty.name)))
    }

    @Test
    @Throws(Exception::class)
    fun canSerialiseAndDeserialiseDefaultValue() {
        val doubler = Property("doubler", { it.toInt() }, { (it * 2).toString() })
        val configuration = ConfigurationTemplate().withProp(doubler, 100).reify()
        assertThat(configuration.valueOf(doubler), equalTo(200))
    }

    @Test
    @Throws(Exception::class)
    fun usesEnvironmentValueInPreferenceToASystemProperty() {
        System.setProperty(envProperty.name, "NOTTHEENVUSER")
        val configuration = ConfigurationTemplate().requiring(envProperty).reify()
        assertThat(configuration.valueOf(envProperty), equalTo(System.getenv(envProperty.name)))
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
}
