package com.yonatankarp.kotlin.spring.boot.template

import com.yonatankarp.kotlin.spring.boot.template.application.ports.input.Greet
import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.EventPublisher
import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.GreetingCatalog
import com.yonatankarp.kotlin.spring.boot.template.application.usecases.GreetUseCase
import org.springframework.beans.factory.BeanRegistrarDsl

class UseCaseBeans :
    BeanRegistrarDsl({
        registerBean<Greet> { GreetUseCase(bean<GreetingCatalog>(), bean<EventPublisher>()) }
    })
