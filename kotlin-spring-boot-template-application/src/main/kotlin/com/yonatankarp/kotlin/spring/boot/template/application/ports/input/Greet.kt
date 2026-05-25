package com.yonatankarp.kotlin.spring.boot.template.application.ports.input

import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting

fun interface Greet {
    suspend operator fun invoke(): Greeting
}
