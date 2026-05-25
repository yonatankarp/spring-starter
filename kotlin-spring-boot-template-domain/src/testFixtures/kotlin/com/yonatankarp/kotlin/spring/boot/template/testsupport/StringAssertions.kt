package com.yonatankarp.kotlin.spring.boot.template.testsupport

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank

/**
 * Assert that a nullable string is both non-null and non-blank, returning
 * the smart-cast non-null value for further chaining.
 *
 * Useful for asserting required JSON fields in `WebTestClient.jsonPath`
 * callbacks where the value arrives typed as `String?`.
 */
fun String?.shouldNotBeNullOrBlank(): String = this.shouldNotBeNull().also { it.shouldNotBeBlank() }
