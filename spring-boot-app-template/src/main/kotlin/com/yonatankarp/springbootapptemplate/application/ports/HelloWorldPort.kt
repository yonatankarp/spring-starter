package com.yonatankarp.springbootapptemplate.application.ports

interface HelloWorldPort {
    suspend fun greet(): String
}
