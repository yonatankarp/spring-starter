plugins {
    `maven-publish`
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("bootJar") {
                groupId = "com.yonatankarp"
                artifactId = project.name
                version = project.version.toString()
                artifact(tasks.named("bootJar"))
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/yonatankarp/spring-starter")
                credentials {
                    username = (project.findProperty("gpr.user") as String?) ?: System.getenv("GITHUB_ACTOR")
                    password = (project.findProperty("gpr.key") as String?) ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
