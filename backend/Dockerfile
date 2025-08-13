# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Build Spring Boot app (skip tests for faster build)
RUN mvn clean package -DskipTests

# Expose backend port
EXPOSE 8080

# Run the Spring Boot app
CMD ["sh", "-c", "java -jar target/rbpt-1.0.0.jar"]


