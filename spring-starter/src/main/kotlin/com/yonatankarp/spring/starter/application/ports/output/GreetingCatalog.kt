package com.yonatankarp.spring.starter.application.ports.output

import com.yonatankarp.spring.starter.domain.valueobject.Greeting

fun interface GreetingCatalog {
    suspend fun random(): Greeting?
}
