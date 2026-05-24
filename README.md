# kotlin-spring-boot-template

[build-badge]: https://github.com/yonatankarp/kotlin-spring-boot-template/actions/workflows/build.yml/badge.svg
[build-state]: https://github.com/yonatankarp/kotlin-spring-boot-template/actions/workflows/build.yml

[quality-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=alert_status
[quality-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[maintainability-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=sqale_rating
[maintainability-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[tech-debt-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=sqale_index
[tech-debt-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[security-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=security_rating
[security-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[vulnerabilities-badge]: https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_spring-starter&metric=vulnerabilities
[vulnerabilities-state]: https://sonarcloud.io/summary/new_code?id=yonatankarp_spring-starter
[license-badge]: https://img.shields.io/badge/License-MIT-yellow.svg
[license-link]: https://opensource.org/licenses/MIT

[spring-boot-badge]: https://img.shields.io/badge/Spring%20Boot-4-6DB33F?logo=springboot&logoColor=white
[spring-boot-link]: https://spring.io/projects/spring-boot
[kotlin-badge]: https://img.shields.io/badge/Kotlin-2-7F52FF?logo=kotlin&logoColor=white
[kotlin-link]: https://kotlinlang.org/
[jdk-badge]: https://img.shields.io/badge/JDK-25-007396?logo=openjdk&logoColor=white
[jdk-link]: https://openjdk.org/
[gradle-badge]: https://img.shields.io/badge/Gradle-9-02303A?logo=gradle&logoColor=white
[gradle-link]: https://gradle.org/
[postgres-badge]: https://img.shields.io/badge/Postgres-18-336791?logo=postgresql&logoColor=white
[postgres-link]: https://www.postgresql.org/
[jooq-badge]: https://img.shields.io/badge/jOOQ-3-FF6F00
[jooq-link]: https://www.jooq.org/
[r2dbc-badge]: https://img.shields.io/badge/R2DBC-1-2C7BB6
[r2dbc-link]: https://r2dbc.io/
[kotest-badge]: https://img.shields.io/badge/Kotest-6-3DA639
[kotest-link]: https://kotest.io/

| **Type**     | **Status**                                                                                                                                                                             |
|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| CI pipelines | [![Build][build-badge]][build-state]                                                                                                                                                   |
| Stack        | [![Spring Boot][spring-boot-badge]][spring-boot-link] [![Kotlin][kotlin-badge]][kotlin-link] [![JDK][jdk-badge]][jdk-link] [![Gradle][gradle-badge]][gradle-link] [![Postgres][postgres-badge]][postgres-link] [![jOOQ][jooq-badge]][jooq-link] [![R2DBC][r2dbc-badge]][r2dbc-link] [![Kotest][kotest-badge]][kotest-link] |
| Maintenance  | [![Quality Gate Status][quality-badge]][quality-state] [![Maintainability Rating][maintainability-badge]][maintainability-state] [![Technical Debt][tech-debt-badge]][tech-debt-state] |
| Security     | [![Security Rating][security-badge]][security-state] [![Vulnerabilities][vulnerabilities-badge]][vulnerabilities-state]                                                                |
| License      | [![License: MIT][license-badge]][license-link]                                                                                                                                         |

Pinned versions live in [`gradle/libs.versions.toml`](./gradle/libs.versions.toml) — badges show the major-version line.



## Purpose

This is a template to fast and easily bootstrap new spring boot web
applications that including full build based on Gradle, CI based on GitHub
actions, and containers based on Docker

For the C4 diagram of the system visit [docs/c4/README.md](./docs/c4/README.md).

## What's inside

The template is a working application with a minimal setup. It contains:

- application skeleton
- setup script to prepare project
- common plugins and libraries
- docker & docker-compose setup
- code quality tools already set up
- integration with GitHub Actions
- integration with Dependabot
- integration with SonarCloud
- Integration with PostgreSQL, Flyway & Jooq
- Integration with test containers
- health check, readiness & liveness probes for k8s integration
- OpenApi spec code generation & documentation
- C4 model architecture diagrams
- MIT license and contribution information

## Setup

Located in `./bin/init.py`. Run it and follow the prompts. The script
self-destructs when finished. Requires Python 3.

### Enable All CI Pipelines

- To enable all CI pipelines, set `REVIEWER_GITHUB_TOKEN` in the repository secrets both for `Action` and `Dependabot`
- Create a new `Ruleset` for the `main` branch and for dependabot (example: [branch rules settings](https://github.com/yonatankarp/kotlin-spring-boot-template/settings/rules))
- Enable Auto-merge on the repository for automatic merge of dependabot pull requests

## Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes. See deployment for notes on
how to deploy the project on a live system.

### Prerequisites

To run the project you need to install the following:

- JDK 25 or newer
- Docker


### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```shell
  ./gradlew build
```

### Running the application

Create the image of the application by executing the following command:

```shell
  ./gradlew assemble
```

You can run this project directly from Gradle by executing the following
command:

```shell
./gradlew bootRun
```

Otherwise, you can create docker image:

```shell
  docker compose build
```

For Apple M1 processor run the following instead:

```shell
DOCKER_BUILDKIT=0 docker compose build
```

Run the distribution (created in `kotlin-spring-boot-template/build/install/kotlin-spring-boot-template`
directory) by executing the following command:

```shell
  docker compose up
```

This will start the API container exposing the application's port
(set to `8080` in this app).

### Smoke test

Once the service is up:

```shell
curl http://localhost:8080/greetings/random   # {"language":"en","message":"Hello, World!"}
curl http://localhost:9001/actuator/health    # {"status":"UP"}
curl http://localhost:9001/actuator/prometheus | head -30
```

Application HTTP runs on port `8080`, management endpoints (health, metrics,
Prometheus) on port `9001` — they're intentionally split so the management
port can sit on an internal/ops network in production.

### API Documentation

Once the service is up, you can access the API documentation from your browser
at the following URL: `http://localhost:8080/api-docs`.

You can use this page as a playground to test the API, and the interactions with
the service.

## Running the tests

You can run the project tests via Gradle by executing the following command:

```shell
./gradlew test
```

### And coding style tests

This project uses the [Spotless Gradle plugin](https://github.com/diffplug/spotless)
to enforce its code style. CI runs `spotlessCheck` and fails if any file is
not canonically formatted.

The project's git hooks (currently `bin/hooks/pre-commit`, which runs
`spotlessApply` on staged Kotlin files) are installed automatically the
first time you run `./gradlew build` — it sets `git config core.hooksPath
bin/hooks` for the repository.

Skip the auto-install with `./gradlew build -PskipGitHooks=true` (or set
`CI=true` in the environment, which is detected automatically).

You can also run Spotless manually:

```shell
./gradlew spotlessApply    # rewrite files to the canonical format
./gradlew spotlessCheck    # report violations without changing files
```

## Plugins

To read more about the plugins included in this project click
[here](docs/plugins.md).

## Built with

- [Kotlin](https://kotlinlang.org/)
- [Spring Boot](https://spring.io/projects/spring-boot) (WebFlux + reactive)
- [jOOQ](https://www.jooq.org/) — type-safe SQL DSL
- [R2DBC](https://r2dbc.io/) — reactive DB driver
- [Flyway](https://flywaydb.org/) — JDBC-based schema migrations at startup
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html) + [Micrometer](https://micrometer.io/) — health, metrics, Prometheus exposition
- [Kotest](https://kotest.io/) (assertions) + [MockK](https://mockk.io/) + [JUnit 5](https://junit.org/junit5/) — testing
- [Testcontainers](https://testcontainers.com/) — integration tests against real Postgres
- [Gradle](https://gradle.org/)
- [Docker](https://www.docker.com/)
- [GitHub Actions](https://docs.github.com/en/actions)

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available,
see the [tags on this repository](https://github.com/your/project/tags).

## Authors

- **Yonatan Karp-Rudin** - *Initial work* - [yonatankarp](https://github.com/yonatankarp)
