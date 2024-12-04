# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/eventsProject-1.0.0-SNAPSHOT.jar /app/eventsProject-1.0.0-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "eventsProject-1.0.0-SNAPSHOT.jar"]