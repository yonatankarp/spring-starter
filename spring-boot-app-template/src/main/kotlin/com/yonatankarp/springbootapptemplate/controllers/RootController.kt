package com.yonatankarp.springbootapptemplate.controllers

import com.yonatankarp.springbootapptemplate.openapi.v1.DemoApiV1Api
import com.yonatankarp.springbootapptemplate.openapi.v1.models.DemoResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController

/**
 * Default endpoints per application.
 */
@RestController
class RootController : DemoApiV1Api<Any> {
    override fun helloWorld(): ResponseEntity<Any> =
        ok(DemoResponse("hello world"))
}
