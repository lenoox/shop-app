FROM arm64v8/openjdk:21-bullseye
WORKDIR /app
COPY . .
RUN ./gradlew clean build
EXPOSE 8080
CMD ["java", "-jar", "./build/libs/demo-0.0.1-SNAPSHOT.jar"]