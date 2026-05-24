package com.yonatankarp.spring.starter.domain.valueobject

import java.util.IllformedLocaleException
import java.util.Locale

data class Greeting(
    val language: String,
    val message: String,
) {
    init {
        require(language.isValidLanguageTag()) {
            "language must be a valid BCP-47 language tag, got: '$language'"
        }
        require(message.isNotBlank()) { "message must not be blank" }
    }
}

private fun String.isValidLanguageTag(): Boolean {
    if (isBlank()) return false
    return try {
        Locale.Builder().setLanguageTag(this).build()
        true
    } catch (_: IllformedLocaleException) {
        false
    }
}
