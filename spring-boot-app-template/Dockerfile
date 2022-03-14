FROM eclipse-temurin:17-jre-alpine

ENV APP_BASE="/home"
ENV APP_NAME="spring-boot-app-template"
ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

RUN mkdir -p ${APP_BASE}

COPY ${APP_NAME}/build/libs/${APP_NAME}*.jar ${APP_BASE}/${APP_NAME}.jar

ENTRYPOINT java $JAVA_OPTS\
    -jar ${APP_BASE}/${APP_NAME}.jar
