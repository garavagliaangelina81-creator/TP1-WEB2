# ---- build ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp package -DskipTests

# ---- run ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# PostgreSQL necesita una zona horaria estándar
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=UTC"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# tiene dos etapas, la primera compila con maven y la segunda esuna imagen liviana que solo tare java y el .jar
# los test se saltan en el build, pq ya los corre el CI, y ademas dentro de docker no hay docker para testcontainers