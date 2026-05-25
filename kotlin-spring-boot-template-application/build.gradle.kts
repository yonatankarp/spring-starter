plugins {
    jacoco
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
    api(project(":kotlin-spring-boot-template-domain"))

    implementation(platform(libs.springboot.bom))

    implementation(libs.kotlin.core)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(platform(libs.springboot.bom))
    testImplementation(libs.bundles.test.unit)
    testImplementation(testFixtures(project(":kotlin-spring-boot-template-domain")))
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
