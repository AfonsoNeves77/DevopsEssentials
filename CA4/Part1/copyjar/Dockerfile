# Use an official OpenJDK image as the base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy JAR files to the working directory
COPY basic_demo-0.1.0.jar /app

# Expose the port the server runs on (replace 8080 with the correct port if needed)
EXPOSE 8080

# Run the server using the specified command
CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
