import org.openapitools.generator.gradle.plugin.tasks.GenerateTask as GenerateOpenApiTask

plugins {
    id("com.revolut.jooq-docker")
    id("io.spring.dependency-management")
    id("jacoco")
    id("org.openapi.generator")
    id("org.springframework.boot")
    id("spring-boot-app-template.code-metrics")
    kotlin("jvm")
    kotlin("plugin.spring")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
        target { JavaLanguageVersion.of(libs.versions.jvmTarget.get()) }
    }
}

dependencies {
    // Spring Boot
    implementation(libs.bundles.springboot.all)

    // Kotlin
    implementation(libs.bundles.kotlin.all)

    // Persistence
    runtimeOnly(libs.postgresql)
    jdbc(libs.postgresql) // Required to generate JOOQ models
    implementation(libs.bundles.persistence.support.all)

    // Documentation
    implementation(libs.springdoc.openapi.starter)

    // Tests
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockk.spring)

    testImplementation(libs.testcontainers.jupiter)
    testImplementation(libs.testcontainers.postgres)
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.18.3")
    }
}


tasks {
    getByName<Jar>("jar") {
        enabled = false
    }

    build {
        finalizedBy(spotlessApply)
    }

    withType<Test> {
        useJUnitPlatform()
        finalizedBy(spotlessApply)
        finalizedBy(jacocoTestReport)
    }

    jacoco {
        toolVersion = libs.versions.jacoco.get()
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

val tasksDependencies = mapOf(
    "spotlessKotlin" to listOf("compileKotlin", "compileTestKotlin", "test", "jacocoTestReport")
)

tasksDependencies.forEach { (taskName, dependencies) ->
    tasks.findByName(taskName)?.dependsOn(dependencies)
}

/********************************************/
/********* OPEN API SPEC GENERATION *********/
/********************************************/

val apiDirectoryPath = "$projectDir/src/main/resources/api"
val openApiGenerateOutputDir = "$buildDir/generated/openapi"

data class ApiSpec(
    val name: String,
    val taskName: String,
    val directoryPath: String,
    val outputDir: String,
    val specFileName: String,
    val generatorType: String,
    val packageName: String,
    val modelPackageName: String,
    val config: Map<String, String> = emptyMap(),
    val templatesDir: String? = null
)

/**
 * List of all api specs to generate
 */
val supportedApis = listOf(
    ApiSpec(
        name = "Demo API",
        taskName = "generateDemoApi",
        directoryPath = apiDirectoryPath,
        outputDir = "$openApiGenerateOutputDir/demo-api",
        specFileName = "demo-api.yaml",
        generatorType = "kotlin-spring",
        packageName = "com.yonatankarp.springbootapptemplate.openapi.v1",
        modelPackageName = "com.yonatankarp.springbootapptemplate.openapi.v1.models",
        templatesDir = "$apiDirectoryPath/templates/kotlin-spring",
        config = mapOf(
            "dateLibrary" to "java8",
            "enumPropertyNaming" to "UPPERCASE",
            "interfaceOnly" to "true",
            "implicitHeaders" to "true",
            "hideGenerationTimestamp" to "true",
            "useTags" to "true",
            "documentationProvider" to "none",
            "useSpringBoot3" to "true",
        ),
    ),
)

// Iterate over the api list and register them as generator tasks
supportedApis.forEach { api ->
    tasks.create(api.taskName, GenerateOpenApiTask::class) {
        group = "openapi tools"
        description = "Generate the code for ${api.name}"

        generatorName.set(api.generatorType)
        inputSpec.set("${api.directoryPath}/${api.specFileName}")
        outputDir.set(api.outputDir)
        apiPackage.set(api.packageName)
        modelPackage.set(api.modelPackageName)
        configOptions.set(api.config)
        api.templatesDir?.let { templateDir.set(it) }
    }
}

tasks {
    register("cleanGeneratedCodeTask") {
        description = "Removes generated Open API code"
        group = "openapi tools"

        doLast {
            logger.info("Cleaning up generated code")
            File(openApiGenerateOutputDir).deleteRecursively()
        }
    }

    clean {
        dependsOn("cleanGeneratedCodeTask")
        supportedApis.forEach { finalizedBy(it.taskName) }
    }

    compileKotlin {
        supportedApis.forEach { dependsOn(it.taskName) }
    }
}

supportedApis.forEach {
    sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].java {
        srcDir("${it.outputDir}/src/main/kotlin")
    }
}

/********************************************/
/******* OPEN API SPEC GENERATION END *******/
/********************************************/
