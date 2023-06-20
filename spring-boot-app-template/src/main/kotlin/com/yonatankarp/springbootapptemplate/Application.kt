package com.yonatankarp.springbootapptemplate

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootAppTemplateApplication

fun main(args: Array<String>) {
    runApplication<SpringBootAppTemplateApplication>(*args)
}
