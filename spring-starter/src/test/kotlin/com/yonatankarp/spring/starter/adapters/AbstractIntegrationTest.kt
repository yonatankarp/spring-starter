package com.yonatankarp.spring.starter.adapters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.postgresql.PostgreSQLContainer

abstract class AbstractIntegrationTest {
    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer("postgres:18-alpine")

        @DynamicPropertySource
        @JvmStatic
        @Suppress("unused")
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("db.host") { postgreSQLContainer.host }
            registry.add("db.port") { postgreSQLContainer.firstMappedPort }
            registry.add("db.schema") { postgreSQLContainer.databaseName }
            registry.add("db.username") { postgreSQLContainer.username }
            registry.add("db.password") { postgreSQLContainer.password }
        }
    }
}
