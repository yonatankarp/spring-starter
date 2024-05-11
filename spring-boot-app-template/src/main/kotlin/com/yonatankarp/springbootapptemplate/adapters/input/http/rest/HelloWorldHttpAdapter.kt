package com.yonatankarp.springbootapptemplate.adapters.input.http.rest

import com.yonatankarp.springbootapptemplate.application.ports.HelloWorldPort
import com.yonatankarp.springbootapptemplate.openapi.v1.DemoApiV1Api
import com.yonatankarp.springbootapptemplate.openapi.v1.models.DemoResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * Default endpoints per application.
 */
@RestController
class HelloWorldHttpAdapter(private val helloWorldPort: HelloWorldPort) :
    DemoApiV1Api<Any> {
    override suspend fun helloWorld(): ResponseEntity<Any> =
        helloWorldPort.greet().let {
            ResponseEntity.ok(DemoResponse(it))
        }
}
