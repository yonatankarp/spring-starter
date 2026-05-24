import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask as GenerateOpenApiTask

plugins {
    jacoco
    id("kotlin-spring-boot-template.spotless")
    id("kotlin-spring-boot-template.publishing-conventions")
    alias(libs.plugins.jooq)
    alias(libs.plugins.springboot.dependency.management)
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.springboot)
    alias(libs.plugins.kotlin.jvm) apply true
    alias(libs.plugins.kotlin.spring) apply true
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
        target { JavaLanguageVersion.of(libs.versions.jvmTarget.get()) }
    }
}

// Read the jOOQ version that Spring Boot manages, this is a workaround until Spring updates their JOOQ version to 3.20.x
val bootManagedJooq: String by lazy {
    (extensions.getByName("dependencyManagement") as DependencyManagementExtension)
        .importedProperties["jooq.version"] as String
}

dependencies {
    // Spring Boot
    implementation(libs.bundles.springboot.all)

    // Kotlin
    implementation(libs.bundles.kotlin.all)

    // Persistence
    jooqCodegen("org.jooq:jooq-codegen:$bootManagedJooq")
    runtimeOnly(libs.postgresql)
    jooqCodegen(libs.postgresql) // Required to generate JOOQ models
    implementation(libs.bundles.persistence.support.all)

    // Documentation
    implementation(libs.springdoc.openapi.starter)

    // Tests
    testImplementation(platform(libs.testcontainers.bom))
    testImplementation(libs.bundles.test.unit)
    testImplementation(libs.bundles.test.integration) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    jar {
        enabled = false
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    generateJooqClasses {
        basePackageName.set("com.yonatankarp.kotlin.spring.boot.template.jooq")
        withContainer {
            image {
                name = "postgres:18-alpine"
            }
        }
        usingJavaConfig {
            withName("org.jooq.codegen.KotlinGenerator")
            generate.apply {
                withKotlinNotNullRecordAttributes(true)
                withDeprecated(false)
            }
        }
    }
}

/********************************************/
/********* OPEN API SPEC GENERATION *********/
/********************************************/

val apiDirectoryPath = "$projectDir/src/main/resources/api"
val openApiGenerateOutputDir = "${layout.buildDirectory.get()}/generated/openapi"

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
        name = "Greetings API",
        taskName = "generateGreetingsApi",
        directoryPath = apiDirectoryPath,
        outputDir = "$openApiGenerateOutputDir/greetings-api",
        specFileName = "greetings-api.yaml",
        generatorType = "kotlin-spring",
        packageName = "com.yonatankarp.kotlin.spring.boot.template.openapi.v1",
        modelPackageName = "com.yonatankarp.kotlin.spring.boot.template.openapi.v1.models",
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
            "reactive" to "true",
        ),
    ),
)

// Iterate over the api list and register them as generator tasks
supportedApis.forEach { api ->
    tasks.register<GenerateOpenApiTask>(api.taskName) {
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
