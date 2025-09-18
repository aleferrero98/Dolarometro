# Use an official base image with JRE 17 from Docker Hub
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/dolarometro.jar /app/dolarometro.jar

# Expose the port your application runs on
EXPOSE 7777

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "/app/dolarometro.jar"]
