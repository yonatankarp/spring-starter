package com.yonatankarp.kotlin.spring.boot.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@ConfigurationPropertiesScan
@Import(UseCaseBeans::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
