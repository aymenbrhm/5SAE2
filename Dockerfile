# Use an official Java runtime (JDK 17) as a parent image
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/eventsProject-1.0.0-SNAPSHOT.jar /app/eventsProject-1.0.0-SNAPSHOT.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "eventsProject-1.0.0-SNAPSHOT.jar"]
