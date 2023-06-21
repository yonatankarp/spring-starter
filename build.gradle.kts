plugins {
    id("com.diffplug.spotless") version libs.versions.spotless apply true
    id("com.revolut.jooq-docker") version libs.versions.jooq.docker.plugin apply false
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management apply false
    id("jacoco")
    id("org.openapi.generator") version libs.versions.swagger apply false
    id("org.springframework.boot") version libs.versions.springboot apply false
    id("spring-boot-app-template.code-metrics")
    id("spring-boot-app-template.java-conventions")
    id("spring-boot-app-template.publishing-conventions")
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("plugin.spring") version libs.versions.kotlin apply false
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
