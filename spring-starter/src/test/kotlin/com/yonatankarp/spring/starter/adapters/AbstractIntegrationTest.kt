package com.yonatankarp.spring.starter.adapters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.postgresql.PostgreSQLContainer

abstract class AbstractIntegrationTest {
    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer("postgres:latest")

        @DynamicPropertySource
        @JvmStatic
        @Suppress("unused")
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgreSQLContainer.jdbcUrl }
            registry.add("spring.datasource.username") { postgreSQLContainer.username }
            registry.add("spring.datasource.password") { postgreSQLContainer.password }
        }
    }
}
