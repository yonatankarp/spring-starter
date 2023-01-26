plugins {
    id("jacoco")
    id("pmd")
    id("spring-boot-app-template.java-conventions")
    id("spring-boot-app-template.code-metrics")
    id("spring-boot-app-template.publishing-conventions")
    id("com.diffplug.spotless") version "6.14.0" apply true
    id("org.springframework.boot") version "3.0.2" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    val kotlinVersion = "1.8.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
        maven {
            url = uri("https://maven.pkg.github.com/yonatankarp/spring-boot-app-template")
            credentials {
                username = findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
