package com.yonatankarp.kotlin.spring.boot.template.adapters.output.persistence

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Flyway runs JDBC-only; the rest of the app talks to the DB over R2DBC.
 *
 * Spring Boot's `FlywayAutoConfiguration` does not activate when only
 * `spring-boot-starter-data-r2dbc` is on the classpath (no JDBC starter
 * means no auto-configured `DataSource` to satisfy its conditions). We
 * wire Flyway programmatically using its standalone configuration API,
 * which uses Flyway's own `DriverDataSource` and needs only the JDBC
 * driver (e.g. `org.postgresql:postgresql`) at runtime.
 */
@Configuration
class FlywayConfig {
    @Bean(initMethod = "migrate")
    fun flyway(db: DbProperties): Flyway =
        Flyway
            .configure()
            .dataSource(db.jdbcUrl, db.username, db.password)
            .load()
}
