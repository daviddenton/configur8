package io.github.konfigur8

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import kotlin.test.fail

class ConfigurationTemplateTest {

    private val FOO = Property.string("FOO")

    @Test
    fun requiringPropsIsImmutable() {
        try {
            val originalConfig = ConfigurationTemplate().requiring(FOO)
            val updatedConfig = ConfigurationTemplate().withProp(FOO, "foo")

            assertThat(updatedConfig.reify().valueOf(FOO), equalTo("foo"))

            originalConfig.reify()
            fail("expected exception")
        } catch(e: Misconfiguration) {
            assertThat(e.message!!, equalTo("No value supplied for key 'FOO'"))
        }
    }

    @Test
    fun overridingPropsIsImmutable() {
        val originalConfig = ConfigurationTemplate().withProp(FOO, "foo")
        val updatedConfig = originalConfig.withProp(FOO, "bar")

        assertThat(originalConfig.reify().valueOf(FOO), equalTo("foo"))
        assertThat(updatedConfig.reify().valueOf(FOO), equalTo("bar"))
    }

}