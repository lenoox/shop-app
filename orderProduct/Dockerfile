FROM arm64v8/openjdk:21-bullseye
WORKDIR /app
COPY . .
RUN ./gradlew clean build
EXPOSE 8090
CMD ["java", "-jar", "./build/libs/orderProduct-0.0.1-SNAPSHOT.jar"]