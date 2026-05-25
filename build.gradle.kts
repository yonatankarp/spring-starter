plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.springboot.dependency.management) apply false
    alias(libs.plugins.springboot) apply false
    alias(libs.plugins.jooq) apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
        maven {
            url = uri("https://maven.pkg.github.com/yonatankarp/kotlin-spring-boot-template")
            credentials {
                username = findProperty("gpr.user")?.toString() ?: System.getenv("GITHUB_ACTOR")
                password = findProperty("gpr.key")?.toString() ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.register<Exec>("installGitHooks") {
    group = "git hooks"
    description = "Configure git to use bin/hooks/ as the hooks directory."

    onlyIf {
        val skip = providers.gradleProperty("skipGitHooks").orNull == "true"
        val ci = System.getenv("CI") != null || System.getenv("GITHUB_ACTIONS") != null
        !skip && !ci
    }
    commandLine("git", "config", "core.hooksPath", "bin/hooks")
}

tasks.register("build") {
    group = "build"
    description = "Aggregate build across all modules"
    dependsOn("installGitHooks")
    subprojects.forEach { dependsOn("${it.path}:build") }
}
