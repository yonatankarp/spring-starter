package com.yonatankarp.spring.starter.adapters.input.http.rest

import com.yonatankarp.spring.starter.adapters.AbstractIntegrationTest
import com.yonatankarp.spring.starter.testsupport.shouldNotBeNullOrBlank
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = ALL)
class GreetEndToEndTest(
    private val client: WebTestClient,
) : AbstractIntegrationTest() {
    @Test
    fun `GET greetings random returns a greeting with language and message`() {
        // Given - Flyway has seeded the catalog against the testcontainer

        // When + Then
        client
            .get()
            .uri("/greetings/random")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.language")
            .value<String> { it.shouldNotBeNullOrBlank() }
            .jsonPath("$.message")
            .value<String> { it.shouldNotBeNullOrBlank() }
    }
}
