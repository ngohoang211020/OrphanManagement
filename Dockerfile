FROM openjdk:11

# Set the working directory
WORKDIR /app

COPY target/orphan-management.jar .
ENTRYPOINT ["java", "-jar", "orphan-management.jar"]

EXPOSE 9000