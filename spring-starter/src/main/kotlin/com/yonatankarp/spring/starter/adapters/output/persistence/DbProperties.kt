package com.yonatankarp.spring.starter.adapters.output.persistence

import org.springframework.boot.context.properties.ConfigurationProperties

enum class DbType(
    val jdbcPrefix: String,
    val r2dbcPrefix: String,
) {
    POSTGRESQL("jdbc:postgresql://", "r2dbc:postgresql://"),
    MYSQL("jdbc:mysql://", "r2dbc:mysql://"),
    MARIADB("jdbc:mariadb://", "r2dbc:mariadb://"),
}

@ConfigurationProperties(prefix = "db")
data class DbProperties(
    val type: DbType,
    val host: String,
    val port: Int,
    val schema: String,
    val username: String,
    val password: String,
) {
    val jdbcUrl: String get() = "${type.jdbcPrefix}$host:$port/$schema"
    val r2dbcUrl: String get() = "${type.r2dbcPrefix}$host:$port/$schema"
}
