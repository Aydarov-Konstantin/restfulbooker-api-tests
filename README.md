<div align="center">

# Restful Booker API Test Automation

Portfolio project for API test automation of the [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html) booking service

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![REST Assured](https://img.shields.io/badge/REST_Assured-5.5.0-6DB33F?logo=java&logoColor=white)](https://rest-assured.io/)
[![JUnit](https://img.shields.io/badge/JUnit-5.11.4-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![AssertJ](https://img.shields.io/badge/AssertJ-3.26.3-8A2BE2)](https://assertj.github.io/doc/)
[![Allure](https://img.shields.io/badge/Allure_Report-2.29.1-FFB400?logo=allure&logoColor=white)](https://allurereport.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)

</div>

> The project validates the Restful Booker public API: service availability, booking retrieval, authentication, and the complete booking lifecycle.

## Tech Stack

- Java 21
- REST Assured
- JUnit 5
- AssertJ
- Allure Report
- AspectJ Weaver
- Maven
- GitHub Actions

## Test Scenarios

1. `API availability`

   Verifies that the booking endpoint is available and returns HTTP `200`.

2. `Booking identifiers`

   Verifies that the service returns a non-empty collection of booking IDs.

3. `Booking details`

   Verifies the response schema of an existing booking.

4. `Authentication`

   Verifies that valid administrator credentials return an authentication token.

5. `Booking lifecycle`

   Creates a booking, fully updates it, partially updates the guest name, retrieves it, and deletes it.

6. `Unknown booking`

   Verifies that a nonexistent booking returns HTTP `404`.

## Project Structure

```text
src/test/java/com/restfulbooker
├── api/          # REST Assured client methods and Allure steps
├── config/       # Base URL and common request configuration
├── model/        # API request and response models
└── tests/        # JUnit API test scenarios

src/test/resources
└── allure.properties

.github/workflows
└── api-tests.yml # GitHub Actions workflow
```

## Prerequisites

- JDK 21 or later
- Maven 3.9 or later

By default, the project runs against the public Restful Booker service. Override the endpoint when needed:

```bash
mvn clean test -DbaseUrl=https://your-environment.example
```

## Run Tests

Run the following command after cloning the repository:

```bash
mvn clean test
```

## Allure Report

Start a local Allure report server after running the tests:

```bash
mvn allure:serve
```

Generate static report files instead:

```bash
mvn allure:report
```

AspectJ Weaver is enabled in Maven Surefire so Allure records annotated steps in the test results.

## Continuous Integration

GitHub Actions runs the complete API test suite on every push and pull request to `main` or `master`, and can also be started manually. Allure results are saved as workflow artifacts for 14 days.

## Implementation Details

- `BookingClient` isolates HTTP calls from test logic and exposes readable Allure steps.
- `ApiConfig` creates a shared JSON request specification and accepts `baseUrl` as a system property.
- Records model booking payloads without unnecessary boilerplate.
- AssertJ provides fluent assertions for response status codes, schema, and data.
- The lifecycle scenario removes the booking it creates, even if an assertion fails.

## Test Results

<div align="center">

| Tests | Result | Errors | Failures |
| :---: | :----: | :----: | :------: |
| 6 | ✅ Passed | — | — |

</div>

## Author

Aydarov Konstantin
