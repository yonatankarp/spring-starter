package com.yonatankarp.kotlin.spring.boot.template.application.usecases

import com.yonatankarp.kotlin.spring.boot.template.application.ports.input.Greet
import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.GreetingCatalog
import com.yonatankarp.kotlin.spring.boot.template.domain.event.GreetingDelivered
import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GreetUseCase(
    private val catalog: GreetingCatalog,
    private val events: ApplicationEventPublisher,
) : Greet {
    override suspend fun invoke(): Greeting =
        (catalog.random() ?: DEFAULT_GREETING).also {
            events.publishEvent(GreetingDelivered(it, Instant.now()))
        }

    companion object {
        private val DEFAULT_GREETING = Greeting(language = "en", message = "Hello, World!")
    }
}
