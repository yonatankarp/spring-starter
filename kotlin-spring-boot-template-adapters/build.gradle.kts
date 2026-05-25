import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    jacoco
    id("kotlin-spring-boot-template.spotless")
    id("kotlin-spring-boot-template.java-conventions")
    id("kotlin-spring-boot-template.publishing-conventions")
    id("com.yonatankarp.openapi.codegen")
    alias(libs.plugins.jooq)
    alias(libs.plugins.springboot.dependency.management)
    alias(libs.plugins.springboot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
    }
}

// Read the jOOQ version that Spring Boot manages, this is a workaround until Spring updates their JOOQ version to 3.20.x
val bootManagedJooq: String by lazy {
    (extensions.getByName("dependencyManagement") as DependencyManagementExtension)
        .importedProperties["jooq.version"] as String
}

dependencies {
    implementation(project(":kotlin-spring-boot-template-application"))

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
    testImplementation(testFixtures(project(":kotlin-spring-boot-template-domain")))
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

openApiCodegen {
    servers {
        register("greetingsApi") {
            spec.set("greetings-api.yaml")
            packageName.set("com.yonatankarp.kotlin.spring.boot.template.openapi.v1")
            modelPackageName.set("com.yonatankarp.kotlin.spring.boot.template.openapi.v1.models")
            templatesDir.set("$projectDir/src/main/resources/api/templates/kotlin-spring")
        }
    }
}
