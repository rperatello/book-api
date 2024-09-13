
FROM openjdk:17-jdk-slim

ENV MEMCACHED_HOST=localhost

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/book-api.jar

COPY src/main/resources/fileCSV/Books_df.csv /app/fileCSV/Books_df.csv

ENTRYPOINT ["java", "-jar", "/app/book-api.jar"]