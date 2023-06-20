plugins {
    id("jacoco")
    id("pmd")
    id("spring-boot-app-template.java-conventions")
    id("spring-boot-app-template.code-metrics")
    id("spring-boot-app-template.publishing-conventions")
    id("com.diffplug.spotless") version libs.versions.spotless apply true
    id("org.springframework.boot") version libs.versions.springboot apply false
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management apply false
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
