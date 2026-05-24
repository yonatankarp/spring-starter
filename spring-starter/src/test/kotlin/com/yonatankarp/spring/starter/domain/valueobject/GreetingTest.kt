package com.yonatankarp.spring.starter.domain.valueobject

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GreetingTest {
    @ParameterizedTest(name = "accepts BCP-47 tag: {0}")
    @ValueSource(strings = ["en", "fr", "ja", "pt-BR", "en-US", "zh-CN", "eng", "zh-Hant", "de-CH-1996"])
    fun `accepts valid BCP-47 language tags`(language: String) {
        Greeting(language = language, message = "Hello").language shouldBe language
    }

    @ParameterizedTest(name = "rejects malformed language tag: '{0}'")
    @ValueSource(strings = ["en_US", "1en", "", "  "])
    fun `rejects malformed language tags`(language: String) {
        shouldThrow<IllegalArgumentException> {
            Greeting(language = language, message = "Hi")
        }
    }

    @ParameterizedTest(name = "rejects blank or whitespace-only message: '{0}'")
    @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
    fun `rejects blank or whitespace-only message`(message: String) {
        shouldThrow<IllegalArgumentException> {
            Greeting(language = "en", message = message)
        }
    }

    @Test
    fun `two greetings with the same attributes are equal`() {
        val a = Greeting(language = "en", message = "Hi")
        val b = Greeting(language = "en", message = "Hi")

        a shouldBe b
        a.hashCode() shouldBe b.hashCode()
    }

    @Test
    fun `greetings differ when language differs`() {
        val a = Greeting(language = "en", message = "Hi")
        val b = Greeting(language = "fr", message = "Hi")

        a shouldNotBe b
    }
}
