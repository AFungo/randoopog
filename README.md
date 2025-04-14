# RandoopOG

**RandoopOG** is an object generator for property-based testing based on the [Randoop](https://randoop.github.io/randoop/) framework. It uses Randoop's method-sequence generation engine to create complex and valid object instances automatically. This tool is designed to integrate with tools like [jqwikOG](https://github.com/AFungo/jqwikog) and can be used to automatically generate inputs for property-based testing.

## Features
- Automatic generation of Java objects using method sequences.
- Customizable assumptions and filters.
- Designed for integration with property-based testing tools.

## Requirements
- **Java Version**: openjdk 11.0.22 (2024-01-16)

## Build
```bash
./gradlew build
```

## Run Tests
You can run specific test classes using Gradle:
```bash
./gradlew test --tests randoop.main.ParameterizedObjectGeneratorTest.printObjectAndSequenceTest
./gradlew test --tests randoop.main.GeneratorTest
```

## Docker Usage
You can build and run RandoopOG in a Docker container for isolated execution:

### Build Docker Image
```bash
docker build -t randoop-og .
```

### Run a Shell Command in the Container
```bash
docker run -it randoop-og git [Command]  # Replace [Command] with your Git or shell command
```

### Run a Specific Test and Save Output
```bash
docker run -it randoop-og ./gradlew test --tests randoop.main.DataStructureTest > test-log.txt
```

## Contributing
Feel free to fork this repository, suggest improvements, or open issues.

## License
This project is based on Randoop and follows the same license unless stated otherwise.

