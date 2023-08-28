plugins {
    id("jacoco")
    id("spring-boot-app-template.code-metrics")
    id("spring-boot-app-template.java-conventions")
    id("spring-boot-app-template.publishing-conventions")
    alias(libs.plugins.spotless) apply true
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.springboot.dependency.management) apply false
    alias(libs.plugins.springboot) apply false
    alias(libs.plugins.openapi.generator) apply false
    alias(libs.plugins.jooq) apply false
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
