FROM eclipse-temurin:25-jdk-alpine AS extractor

ENV APP_NAME="spring-starter"
WORKDIR /workspace

COPY ${APP_NAME}/build/libs/${APP_NAME}*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted


FROM eclipse-temurin:25-jre-alpine

ENV APP_BASE="/home" \
    APP_NAME="spring-starter" \
    SERVER_PORT="8080" \
    MANAGEMENT_PORT="9001"

EXPOSE ${SERVER_PORT} ${MANAGEMENT_PORT}

RUN apk update && apk upgrade && apk add --no-cache curl openssl gcompat bash busybox-extras iputils

WORKDIR ${APP_BASE}/${APP_NAME}

COPY --from=extractor /workspace/extracted/dependencies/ ./
COPY --from=extractor /workspace/extracted/spring-boot-loader/ ./
COPY --from=extractor /workspace/extracted/snapshot-dependencies/ ./
COPY --from=extractor /workspace/extracted/application/ ./

USER 65534

ENTRYPOINT ["java", "-jar", "application.jar"]
