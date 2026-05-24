plugins {
    id("com.diffplug.spotless")
}

repositories {
    mavenCentral()
}

spotless {
    kotlin {
        target(
            fileTree(projectDir) {
                include("**/*.kt")
                exclude(
                    "**/.gradle/**",
                    "**/build/**",
                )
            },
        )
        ktlint("1.5.0")
    }
}
