package com.yonatankarp.kotlin.spring.boot.template.adapters.output.observability

import com.yonatankarp.kotlin.spring.boot.template.domain.event.GreetingDelivered
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class GreetingDeliveredLogger {
    @EventListener
    fun on(event: GreetingDelivered) {
        log.info {
            "Greeting delivered: language=${event.greeting.language}, " +
                "message='${event.greeting.message}', at=${event.deliveredAt}"
        }
    }
}
