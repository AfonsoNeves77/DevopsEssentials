# Use an official OpenJDK image as the base image
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Install Git to clone the repository
RUN apt-get update && apt-get install -y git

# Clone the repository
RUN git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git .

# Navigate to the desired directory and give execute permissions to the Gradle wrapper
WORKDIR /app/CA2/Part1/gradle_basic_demo
RUN chmod +x ./gradlew && ./gradlew build

# Expose the port the server runs on
EXPOSE 8080

# Run the server using the Gradle task
CMD ["./gradlew", "runServer"]

