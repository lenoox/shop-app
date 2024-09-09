FROM arm64v8/openjdk:21-bullseye
WORKDIR /app
COPY . .
RUN ./gradlew clean build
EXPOSE 8089
CMD ["java", "-jar", "./build/libs/sendEmail-0.0.1-SNAPSHOT.jar"]