# Use OpenJDK 17
FROM openjdk:17-jdk-slim

# Install MongoDB
RUN apt-get update && \
    apt-get install -y mongodb && \
    mkdir -p /data/db

# Set working directory
WORKDIR /app

# Copy backend project files
COPY . .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Build Spring Boot app
RUN ./mvnw clean package -DskipTests

# Expose ports for Spring Boot and MongoDB
EXPOSE 8080 27017

# Start MongoDB and Spring Boot together
CMD ["sh", "-c", "mongod --dbpath /data/db & java -jar target/rbpt-1.0.0.jar"]
