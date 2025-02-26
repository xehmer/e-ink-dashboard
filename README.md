# E-Ink Dashboard

## Testing

### Test Categories

The project uses different categories of tests:

1. **Regular Tests**: Unit tests and basic integration tests that don't require external services or sensitive
   credentials.
    - Run with: `./gradlew test`

2. **Integration Tests**: Tests that require external services, credentials, or special setup.
    - Run with: `./gradlew integrationTest`
    - These tests are excluded from regular builds to:
        - Protect sensitive credentials
        - Avoid external service dependencies in CI/CD pipelines
        - Speed up regular test execution
        - Prevent test failures due to external service availability

### Integration Tests

Some tests, like `GoogleCalendarWidgetDataProviderTest`, are marked with `@Tag("integration")` because they:

- Require sensitive credentials (service account JSON)
- Need access to external services (Google Calendar API)
- Use private configuration (calendar IDs)

To run these tests:

1. Ensure all required credentials are in place:
    - `/src/test/resources/secret/service-account.json`
    - `/src/test/resources/http-client.private.env.json`
2. Execute: `./gradlew integrationTest`

### Running All Tests

To run both regular and integration tests:

```bash
./gradlew test integrationTest
```

Note: Integration tests will fail if required credentials or services are not available.
