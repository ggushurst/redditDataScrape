FROM java:8-jdk-alpine
COPY ./target/URLReaderJava-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "URLReaderJava-1.0-SNAPSHOT-jar-with-dependencies.jar"]
