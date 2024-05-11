package com.yonatankarp.spring.starter.application.ports

interface HelloWorldPort {
    suspend fun greet(): String
}
