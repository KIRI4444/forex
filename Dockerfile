FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/forex-0.0.1-SNAPSHOT.jar /app/forex.jar
ENTRYPOINT ["java", "-jar", "forex.jar"]