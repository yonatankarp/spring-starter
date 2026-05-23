package com.yonatankarp.spring.starter.adapters.output.observability

import com.yonatankarp.spring.starter.domain.event.GreetingDelivered
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

private val log = KotlinLogging.logger {}

@Component
class GreetingDeliveredLogger {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun on(event: GreetingDelivered) {
        log.info {
            "Greeting delivered: language=${event.greeting.language}, " +
                "message='${event.greeting.message}', at=${event.deliveredAt}"
        }
    }
}
