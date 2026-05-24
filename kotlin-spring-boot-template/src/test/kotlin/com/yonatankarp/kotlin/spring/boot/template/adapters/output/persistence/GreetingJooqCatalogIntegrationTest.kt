package com.yonatankarp.kotlin.spring.boot.template.adapters.output.persistence

import com.yonatankarp.kotlin.spring.boot.template.adapters.AbstractIntegrationTest
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class GreetingJooqCatalogIntegrationTest(
    private val catalog: GreetingJooqCatalog,
) : AbstractIntegrationTest() {
    @Test
    fun `random returns a greeting from the seeded catalog`() =
        runTest {
            // Given - Flyway has seeded V1.0.0__init_db.sql against the testcontainer

            // When
            val greeting = catalog.random()

            // Then
            greeting.shouldNotBeNull()
            greeting.language.shouldNotBeBlank()
            greeting.message.shouldNotBeBlank()
            SEEDED_LANGUAGES shouldContain greeting.language
        }

    companion object {
        private val SEEDED_LANGUAGES = setOf("en", "es", "fr", "de", "ja")
    }
}
