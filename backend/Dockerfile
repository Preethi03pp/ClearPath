# Use OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Build Spring Boot app (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose port (Render assigns $PORT automatically)
EXPOSE 8080

# Run Spring Boot app
CMD ["sh", "-c", "java -jar target/rbpt-1.0.0.jar"]



