package com.yonatankarp.kotlin.spring.boot.template.application.usecases

import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.GreetingCatalog
import com.yonatankarp.kotlin.spring.boot.template.domain.event.GreetingDelivered
import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher

class GreetUseCaseTest {
    private val catalog: GreetingCatalog = mockk()
    private val events: ApplicationEventPublisher = mockk(relaxed = true)

    @BeforeEach
    fun resetMocks() {
        clearMocks(catalog, events)
    }

    private fun createUseCase() = GreetUseCase(catalog, events)

    @Test
    fun `returns greeting from catalog and publishes GreetingDelivered`() =
        runTest {
            // Given
            val useCase = createUseCase()
            val expected = Greeting(language = "fr", message = "Bonjour")
            coEvery { catalog.random() } returns expected
            val published = slot<GreetingDelivered>()

            // When
            val result = useCase()

            // Then
            result shouldBe expected
            verify { events.publishEvent(capture(published)) }
            published.captured.greeting shouldBe expected
        }

    @Test
    fun `falls back to default greeting when catalog is empty and still publishes`() =
        runTest {
            // Given
            val useCase = createUseCase()
            coEvery { catalog.random() } returns null
            val published = slot<GreetingDelivered>()

            // When
            val result = useCase()

            // Then
            result shouldBe Greeting(language = "en", message = "Hello, World!")
            verify { events.publishEvent(capture(published)) }
            published.captured.greeting shouldBe result
        }
}
