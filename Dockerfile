FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
COPY . /app
WORKDIR /app
RUN mvn clean install -DskipTests


FROM maven:3.9.4-eclipse-temurin-21-alpine
COPY --from=build /app/target/*.jar /app/app.jar

FROM eclipse-temurin:21-alpine


COPY --from=1 /app/app.jar /app/app.jar


EXPOSE 8080


ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]
