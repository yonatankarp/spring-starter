package com.yonatankarp.spring.starter.domain.valueobject

data class Greeting(
    val language: String,
    val message: String,
) {
    init {
        require(LANGUAGE_REGEX.matches(language)) {
            "language must be BCP-47 (e.g. 'en' or 'en-US'), got: '$language'"
        }
        require(message.isNotBlank()) { "message must not be blank" }
    }

    companion object {
        private val LANGUAGE_REGEX = Regex("^[a-z]{2}(-[A-Z]{2})?$")
    }
}
