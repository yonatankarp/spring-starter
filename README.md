# spring-boot-app-template

[![Build](https://github.com/yonatankarp/spring-boot-template/actions/workflows/ci.yml/badge.svg)](https://github.com/yonatankarp/spring-boot-template/actions/workflows/ci.yml)
[![Linters](https://github.com/yonatankarp/spring-boot-template/actions/workflows/linting.yml/badge.svg)](https://github.com/yonatankarp/spring-boot-template/actions/workflows/linting.yml)

This is a template to fast and easily bootstrap new spring boot web
applications that including full build based on Gradle, CI based on GitHub
actions, and containers based on Docker

## Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes. See deployment for notes on
how to deploy the project on a live system.

### Prerequisites

To run the project you need to install the following:

- JDK 11 or newer
- Docker

### Installing

You can run this project directly from Gradle by executing the following
command:

```shell
./gradlew bootRun
```

Additionally, you can run it via docker by running the following commands:

```shell
docker build --file ./spring-boot-app-template/Dockerfile . --tag spring-boot-app-template:local
docker run spring-boot-app-template:local
```

## Running the tests

You can run the project tests via Gradle by executing the following command:

```shell
./gradlew test
```

### And coding style tests

This project uses [Spotless Gradle plugin](https://github.com/diffplug/spotless)
to enforce its code style. The plugin will run automatically after every
successful build, test, and assemble stage. However, if you would like to run
it manually you can do so by running the following commands:

To apply the code style to the project run:

```shell
./gradlew spotlessApply
```

To check your code without applying any changes you can execute:

```shell
./gradlew spotlessCheck
```

## Plugins

To read more about the plugins included in this project click
[here](./docs/plugins.md).

## Built With

- [OpenJdk 11](https://openjdk.java.net/projects/jdk/11/)
- [Kotlin](https://kotlinlang.org/)
- [SpringBoot](https://spring.io/projects/spring-boot) - The web framework used
- [Gradle](https://gradle.org/) - Dependency Management
- [GitHub Actions](https://docs.github.com/en/actions) - Continuous Integration
- [Docker](https://www.docker.com/) - Container handling

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available,
see the [tags on this repository](https://github.com/your/project/tags).

## Authors

- **Yonatan Karp-Rudin** - *Initial work* - [yonatankarp](https://github.com/yonatankarp)
