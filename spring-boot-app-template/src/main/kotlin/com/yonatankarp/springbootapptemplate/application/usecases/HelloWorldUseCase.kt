package com.yonatankarp.springbootapptemplate.application.usecases

import com.yonatankarp.springbootapptemplate.application.ports.HelloWorldPort
import com.yonatankarp.springbootapptemplate.domain.valueobject.HelloWorld
import org.springframework.stereotype.Service

@Service
class HelloWorldUseCase : HelloWorldPort {
    override suspend fun greet() = HelloWorld().sayHello()
}
