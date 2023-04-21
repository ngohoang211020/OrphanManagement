FROM openjdk:11

# Set the working directory
WORKDIR /app

COPY target/OrphanManagement-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "OrphanManagement-0.0.1-SNAPSHOT.jar"]

EXPOSE 9000