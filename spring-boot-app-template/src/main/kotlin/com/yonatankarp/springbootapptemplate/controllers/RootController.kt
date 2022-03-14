package com.yonatankarp.springbootapptemplate.controllers

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok

import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RestController


/**
 * Default endpoints per application.
 */
@RestController
class RootController {
    /**
     * Root GET endpoint.
     *
     * Azure application service has a hidden feature of making requests to root endpoint when
     * "Always On" is turned on.
     * This is the endpoint to deal with that and therefore silence the unnecessary 404s as a response code.
     *
     * @return Welcome message from the service.
     */
    @GetMapping("/")
    fun welcome(): ResponseEntity<String> = ok("Welcome to spring-boot-app-template")
}
