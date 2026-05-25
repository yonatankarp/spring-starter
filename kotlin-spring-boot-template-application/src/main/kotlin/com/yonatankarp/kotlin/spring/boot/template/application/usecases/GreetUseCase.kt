package com.yonatankarp.kotlin.spring.boot.template.application.usecases

import com.yonatankarp.kotlin.spring.boot.template.application.ports.input.Greet
import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.EventPublisher
import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.GreetingCatalog
import com.yonatankarp.kotlin.spring.boot.template.domain.event.GreetingDelivered
import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting
import java.time.Instant

class GreetUseCase(
    private val catalog: GreetingCatalog,
    private val events: EventPublisher,
) : Greet {
    override suspend fun invoke(): Greeting =
        (catalog.random() ?: DEFAULT_GREETING).also {
            events.publish(GreetingDelivered(it, Instant.now()))
        }

    companion object {
        private val DEFAULT_GREETING = Greeting(language = "en", message = "Hello, World!")
    }
}
