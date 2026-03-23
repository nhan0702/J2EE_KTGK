# Step 1: Build the Application
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy the wrapper and pom.xml first to download dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
# RUN ./mvnw dependency:go-offline -B # Optional optimization step

# Copy the rest of the source code
COPY src src

# Build the application skipping tests for speed
RUN ./mvnw clean package -DskipTests

# Step 2: Run the Application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the compiled JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Define the port the backend binds to
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
