package com.yonatankarp.spring.starter.domain.event

import com.yonatankarp.spring.starter.domain.valueobject.Greeting
import java.time.Instant

data class GreetingDelivered(
    val greeting: Greeting,
    val deliveredAt: Instant,
)
