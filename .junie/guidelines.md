# E-Ink Dashboard Development Guidelines

## Project Overview

The E-Ink Dashboard is a modular web application that renders various widgets for display on e-ink devices. It supports
different types of widgets (weather, calendar, etc.) and uses a flexible rendering system.

## Tech Stack

- **Language**: Kotlin 2.1.0 with Java 21
- **Framework**: Spring Boot 3.4.2
- **Key Libraries**:
    - Spring Modulith: Modular architecture
    - KotlinX HTML: HTML rendering
    - OkHttp: HTTP client
    - JUnit 5: Testing

## Project Structure

```
src/main/kotlin/de/xehmer/dashboard/
├── api/          # API definitions and interfaces
├── core/         # Core dashboard functionality
├── web/          # Web interface components
├── persistence/  # Data storage
├── utils/        # Shared utilities
└── widgets/      # Widget implementations
    ├── weather/
    ├── calendar/
    └── jenah/
```

## Development Setup

1. Ensure Java 21 is installed
2. Clone the repository
3. Run `./gradlew build` to build the project
4. Use `./gradlew bootRun` to start the application
5. Access the dashboard at `http://localhost:8080`

## Testing Guidelines

1. Write tests for new widgets and features
2. Run tests: `./gradlew test`
3. Test coverage includes:
    - Unit tests for widgets
    - Integration tests for services
    - Spring Modulith tests for module boundaries

## Best Practices

1. **Widget Development**:
    - Extend base widget interfaces
    - Keep rendering logic separate from data fetching
    - Follow existing widget patterns

2. **Code Organization**:
    - Place new widgets in dedicated packages
    - Use Spring dependency injection
    - Follow Kotlin coding conventions

3. **Testing**:
    - Test widget rendering
    - Mock external services
    - Verify widget configuration handling
