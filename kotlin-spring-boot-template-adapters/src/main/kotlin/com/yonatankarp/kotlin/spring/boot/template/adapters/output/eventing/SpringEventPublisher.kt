package com.yonatankarp.kotlin.spring.boot.template.adapters.output.eventing

import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.EventPublisher
import com.yonatankarp.kotlin.spring.boot.template.domain.event.DomainEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringEventPublisher(
    private val publisher: ApplicationEventPublisher,
) : EventPublisher {
    override fun publish(event: DomainEvent) = publisher.publishEvent(event)
}
