plugins {
    id("com.revolut.jooq-docker")
    id("io.spring.dependency-management")
    id("jacoco")
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

    // Tests
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockk.spring)

    testImplementation(libs.testcontainers.jupiter)
    testImplementation(libs.testcontainers.postgres)
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.17.3")
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
