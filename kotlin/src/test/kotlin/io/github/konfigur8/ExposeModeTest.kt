package io.github.konfigur8

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class ExposeModeTest {

    @Test
    @Throws(Exception::class)
    fun publicDisplaysValue() {
        assertThat(ExposeMode.Public.display("hello"), equalTo("hello"))
    }

    @Test
    @Throws(Exception::class)
    fun privateMasksValue() {
        assertThat(ExposeMode.Private.display("hello"), equalTo("***********"))
    }
}
