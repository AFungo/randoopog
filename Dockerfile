# Use an official Java development kit as the parent image
FROM openjdk:11-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the local project files into the container's workspace
COPY . .

# Build the application and run tests
# This assumes your application can be built and tested with Gradle
RUN ./gradlew build --no-daemon
RUN ./gradlew test --no-daemon

# You can specify a command to run your application here if needed
# CMD ["java", "-jar", "your-app-build.jar"]

