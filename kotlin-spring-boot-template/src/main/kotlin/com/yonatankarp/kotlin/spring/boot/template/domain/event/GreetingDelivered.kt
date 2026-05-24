package com.yonatankarp.kotlin.spring.boot.template.domain.event

import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting
import java.time.Instant

data class GreetingDelivered(
    val greeting: Greeting,
    val deliveredAt: Instant,
)
