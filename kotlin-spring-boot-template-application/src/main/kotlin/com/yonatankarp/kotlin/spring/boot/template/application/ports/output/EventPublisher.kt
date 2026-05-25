package com.yonatankarp.kotlin.spring.boot.template.application.ports.output

import com.yonatankarp.kotlin.spring.boot.template.domain.event.DomainEvent

fun interface EventPublisher {
    fun publish(event: DomainEvent)
}
