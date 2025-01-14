FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Make sure the mvnw script has executable permissions
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy project files
COPY pom.xml .
# Download dependencies first (this step will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy source files and .env if it exists
COPY src src
COPY .env* ./ 2>/dev/null || echo "No .env file found"

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install required packages for Spring Boot with security
RUN apk add --no-cache tzdata

VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the dependency files
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# Copy .env file from build stage if it exists
COPY --from=build /workspace/app/.env* /app/ 2>/dev/null || echo "No .env file found"

# Set working directory
WORKDIR /app

# Run the application
ENTRYPOINT ["java","-cp",".:lib/*","com.streamingbot.userservice.UserServiceApplication"] 