package com.yonatankarp.kotlin.spring.boot.template.adapters.output.persistence

import com.yonatankarp.kotlin.spring.boot.template.application.ports.output.GreetingCatalog
import com.yonatankarp.kotlin.spring.boot.template.domain.valueobject.Greeting
import com.yonatankarp.kotlin.spring.boot.template.jooq.tables.references.GREETING
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class GreetingJooqCatalog(
    private val jooq: DSLContext,
) : GreetingCatalog {
    override suspend fun random(): Greeting? =
        jooq
            .selectFrom(GREETING)
            .orderBy(DSL.rand())
            .limit(1)
            .awaitFirstOrNull()
            ?.let { Greeting(it.language, it.message) }
}
