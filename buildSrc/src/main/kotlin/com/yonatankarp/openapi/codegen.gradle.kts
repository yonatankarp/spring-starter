package com.yonatankarp.openapi

import org.gradle.api.tasks.SourceSetContainer
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask as GenerateOpenApiTask

plugins {
    id("org.openapi.generator")
}

val openApi = extensions.create<OpenApiCodegenExtension>("openApiCodegen")
openApi.directoryPath.convention("$projectDir/src/main/resources/api")
openApi.outputRoot.convention(layout.buildDirectory.dir("generated/openapi").map { it.asFile.absolutePath })

val serverDefaults =
    mapOf(
        "dateLibrary" to "java8",
        "enumPropertyNaming" to "UPPERCASE",
        "interfaceOnly" to "true",
        "implicitHeaders" to "true",
        "hideGenerationTimestamp" to "true",
        "useTags" to "true",
        "documentationProvider" to "none",
        "useSpringBoot3" to "true",
        "reactive" to "true",
    )

val clientDefaults =
    mapOf(
        "dateLibrary" to "java8",
        "enumPropertyNaming" to "UPPERCASE",
        "hideGenerationTimestamp" to "true",
        "serializationLibrary" to "jackson",
        "useCoroutines" to "true",
    )

fun taskNameFor(name: String) = "generate" + name.replaceFirstChar { it.uppercase() }

fun outputDirFor(root: String, name: String) = "$root/$name"

afterEvaluate {
    val dirPath = openApi.directoryPath.get()
    val outputRoot = openApi.outputRoot.get()

    openApi.servers.forEach { serverSpec ->
        val taskName = taskNameFor(serverSpec.name)
        val output = outputDirFor(outputRoot, serverSpec.name)
        val specFile = serverSpec.spec.get()
        val pkg = serverSpec.packageName.get()
        val modelPkg = serverSpec.modelPackageName.get()
        val cfg = serverDefaults + serverSpec.config.getOrElse(emptyMap())
        val tplDir = serverSpec.templatesDir.orNull

        tasks.register<GenerateOpenApiTask>(taskName) {
            group = "openapi tools"
            description = "Generate server code for $taskName"
            generatorName.set("kotlin-spring")
            inputSpec.set("$dirPath/$specFile")
            outputDir.set(output)
            apiPackage.set(pkg)
            modelPackage.set(modelPkg)
            configOptions.set(cfg)
            tplDir?.let { templateDir.set(it) }
        }
    }

    openApi.clients.forEach { clientSpec ->
        val taskName = taskNameFor(clientSpec.name)
        val output = outputDirFor(outputRoot, clientSpec.name)
        val specFile = clientSpec.spec.get()
        val pkg = clientSpec.packageName.get()
        val modelPkg = clientSpec.modelPackageName.get()
        val library = clientSpec.library.getOrElse("jvm-spring-restclient")
        val cfg = clientDefaults + mapOf("library" to library) + clientSpec.config.getOrElse(emptyMap())
        val tplDir = clientSpec.templatesDir.orNull

        tasks.register<GenerateOpenApiTask>(taskName) {
            group = "openapi tools"
            description = "Generate client code for $taskName"
            generatorName.set("kotlin")
            inputSpec.set("$dirPath/$specFile")
            outputDir.set(output)
            apiPackage.set(pkg)
            modelPackage.set(modelPkg)
            configOptions.set(cfg)
            tplDir?.let { templateDir.set(it) }
        }
    }

    val specNames = openApi.servers.names + openApi.clients.names
    val codegenTaskNames = specNames.map { taskNameFor(it) }

    tasks.register("cleanGeneratedOpenApiCode") {
        description = "Removes generated OpenAPI code"
        group = "openapi tools"
        doLast {
            java.io.File(outputRoot).deleteRecursively()
        }
    }

    tasks.named("clean").configure {
        dependsOn("cleanGeneratedOpenApiCode")
        codegenTaskNames.forEach { finalizedBy(it) }
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks.named("compileKotlin").configure {
            codegenTaskNames.forEach { dependsOn(it) }
        }
    }

    extensions.findByType<SourceSetContainer>()?.named("main")?.configure {
        specNames.forEach { specName ->
            java.srcDir("${outputDirFor(outputRoot, specName)}/src/main/kotlin")
        }
    }
}
