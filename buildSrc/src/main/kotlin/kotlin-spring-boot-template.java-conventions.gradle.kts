plugins {
    java
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.jar {
    archiveFileName.set("${project.name}-${project.version}.jar")
    manifest {
        attributes(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
        )
    }
    from({
        configurations.runtimeClasspath
            .get()
            .filter { it.exists() }
            .map { if (it.isDirectory) it else zipTree(it) }
    })
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/LICENSE.txt")
    includeEmptyDirs = false
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
