package com.yonatankarp.spring.starter.adapters.input.http.rest

import com.yonatankarp.spring.starter.application.ports.input.Greet
import com.yonatankarp.spring.starter.domain.valueobject.Greeting
import com.yonatankarp.spring.starter.openapi.v1.GreetingsV1Api
import com.yonatankarp.spring.starter.openapi.v1.models.GreetingResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingsHttpAdapter(
    private val greet: Greet,
) : GreetingsV1Api<Any> {
    override suspend fun getRandomGreeting(): ResponseEntity<Any> = ResponseEntity.ok(greet().toResponse())
}

private fun Greeting.toResponse() = GreetingResponse(language = language, message = message)
