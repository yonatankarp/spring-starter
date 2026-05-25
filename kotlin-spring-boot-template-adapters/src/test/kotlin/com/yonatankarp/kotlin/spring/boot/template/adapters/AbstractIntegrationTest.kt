package com.yonatankarp.kotlin.spring.boot.template.adapters

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer

abstract class AbstractIntegrationTest {
    companion object {
        private val postgreSQLContainer: PostgreSQLContainer by lazy {
            PostgreSQLContainer("postgres:18-alpine").also { it.start() }
        }

        @DynamicPropertySource
        @JvmStatic
        @Suppress("unused")
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("db.type") { "postgresql" }
            registry.add("db.host") { postgreSQLContainer.host }
            registry.add("db.port") { postgreSQLContainer.firstMappedPort }
            registry.add("db.schema") { postgreSQLContainer.databaseName }
            registry.add("db.username") { postgreSQLContainer.username }
            registry.add("db.password") { postgreSQLContainer.password }
        }
    }
}
