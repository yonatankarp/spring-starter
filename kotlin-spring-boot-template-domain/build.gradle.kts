plugins {
    jacoco
    `java-test-fixtures`
    id("kotlin-spring-boot-template.spotless")
    id("kotlin-spring-boot-template.java-conventions")
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get()))
    }
}

dependencies {
    implementation(libs.kotlin.core)

    testFixturesImplementation(libs.kotlin.core)
    testFixturesImplementation(libs.kotest.assertions.core)

    testImplementation(platform(libs.springboot.bom))
    testImplementation(libs.bundles.test.unit)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
