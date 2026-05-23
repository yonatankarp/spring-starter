package com.yonatankarp.spring.starter.application.usecases

import com.yonatankarp.spring.starter.application.ports.input.Greet
import com.yonatankarp.spring.starter.application.ports.output.GreetingCatalog
import com.yonatankarp.spring.starter.domain.event.GreetingDelivered
import com.yonatankarp.spring.starter.domain.valueobject.Greeting
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class GreetUseCase(
    private val catalog: GreetingCatalog,
    private val events: ApplicationEventPublisher,
) : Greet {
    @Transactional(readOnly = true)
    override suspend fun invoke(): Greeting =
        (catalog.random() ?: DEFAULT_GREETING).also {
            events.publishEvent(GreetingDelivered(it, Instant.now()))
        }

    companion object {
        private val DEFAULT_GREETING = Greeting(language = "en", message = "Hello, World!")
    }
}
