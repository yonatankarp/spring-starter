package com.yonatankarp.spring.starter.adapters.output.persistence

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig {
    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext = DSL.using(connectionFactory, SQLDialect.POSTGRES)
}
