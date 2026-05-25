package com.yonatankarp.openapi

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * DSL entry point for the `openapi.codegen` plugin.
 *
 * ```
 * openApiCodegen {
 *     servers {
 *         register("greetingsApi") {
 *             spec.set("greetings-api.yaml")
 *             packageName.set("...openapi.v1")
 *             modelPackageName.set("...openapi.v1.models")
 *         }
 *     }
 *     clients {
 *         register("paymentsClient") {
 *             spec.set("payments-api.yaml")
 *             packageName.set("...client.payments")
 *             modelPackageName.set("...client.payments.models")
 *         }
 *     }
 * }
 * ```
 *
 * Each registered name becomes both the Gradle task name suffix (`generate<Name>`) and the
 * output directory under [outputRoot].
 */
abstract class OpenApiCodegenExtension
    @Inject
    constructor(objects: ObjectFactory) {
        /** Directory containing OpenAPI spec yaml files. Defaults to `src/main/resources/api`. */
        abstract val directoryPath: Property<String>

        /** Output root for generated code. Defaults to `${'$'}buildDir/generated/openapi`. */
        abstract val outputRoot: Property<String>

        val servers: NamedDomainObjectContainer<ServerSpec> =
            objects.domainObjectContainer(ServerSpec::class.java)

        val clients: NamedDomainObjectContainer<ClientSpec> =
            objects.domainObjectContainer(ClientSpec::class.java)

        fun servers(action: Action<in NamedDomainObjectContainer<ServerSpec>>) {
            action.execute(servers)
        }

        fun clients(action: Action<in NamedDomainObjectContainer<ClientSpec>>) {
            action.execute(clients)
        }
    }
