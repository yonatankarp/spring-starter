package com.yonatankarp.spring.starter.application.ports.input

import com.yonatankarp.spring.starter.domain.valueobject.Greeting

interface Greet {
    suspend operator fun invoke(): Greeting
}
