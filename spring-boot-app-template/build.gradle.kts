plugins {
    id("jacoco")
    id("pmd")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
    kotlin("jvm")
    kotlin("plugin.spring")
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
}
