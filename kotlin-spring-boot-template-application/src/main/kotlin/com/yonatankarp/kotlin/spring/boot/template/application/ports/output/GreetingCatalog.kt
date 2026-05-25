package com.yonatankarp.kotlin.spring.boot.template.application.ports.output

import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting

fun interface GreetingCatalog {
    suspend fun random(): Greeting?
}
