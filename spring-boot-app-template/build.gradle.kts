plugins {
    id("jacoco")
    id("spring-boot-app-template.code-metrics")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        target { JavaLanguageVersion.of(17) }
    }
}

val coroutinesVersion = "1.6.4"
val mockkVersion = "1.13.2"
val mockkSpringVersion = "3.1.1"

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
        finalizedBy(pmdTest)
    }

    jacoco {
        toolVersion = "0.8.7"
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

tasks.findByName("spotlessKotlin")?.dependsOn("compileKotlin")
tasks.findByName("spotlessKotlin")?.dependsOn("compileTestKotlin")
tasks.findByName("spotlessKotlin")?.dependsOn("test")
tasks.findByName("spotlessKotlin")?.dependsOn("jacocoTestReport")
