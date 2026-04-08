# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "claude_practice.claude_practice.ClaudePracticeApplicationTests"

# Run a single test method
./gradlew test --tests "claude_practice.claude_practice.ClaudePracticeApplicationTests.methodName"

# Clean build artifacts
./gradlew clean
```

## Project Overview

This is a Spring Boot 4.0.5 application using Java 21 and Gradle (Kotlin DSL).

- **Group/Package**: `claude_practice.claude_practice`
- **Entry point**: `ClaudePracticeApplication.java` — standard `@SpringBootApplication` bootstrap
- **Test framework**: JUnit 5 via `spring-boot-starter-test`

The project is a fresh Spring Boot scaffold with no additional features yet. New code should be added under `src/main/java/claude_practice/claude_practice/`.
