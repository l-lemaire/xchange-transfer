# xchange-transfer
Fund transfer with currency

## Technologies
- Java 21 (LTS) with Spring Boot 3
- Postgres
- Maven
- Docker

## User guide
- Create a database in Postgres and run the given SQL script to create the necessary tables
- Define the following environment variables:
  - `SPRING_DATASOURCE_URL`: Database URL
  - `SPRING_DATASOURCE_USERNAME`: Database username
  - `SPRING_DATASOURCE_PASSWORD`: Database password
- Run the given jar file with the following command (make sure you have Java 21 installed):
  - `java -jar xchange-transfer-0.0.1-SNAPSHOT.jar`
- The application will be running on [`http://localhost:8080`](http://localhost:8080)

## Development guide
- Clone the repository
- Run the following command to build the project:
  - `mvn clean package`
- Load the project in your favorite IDE and launch the application

### Database
Spring Boot has been configured to create and launch the database using Docker on startup.
Make sure you have Docker installed and running on your machine.

The schema is available in the `src/main/resources/schema.sql` file.
Spring boot can be configured to run this script on startup.
Define the following environment variable to enable this feature: SPRING_SQL_INIT_MODE=ALWAYS

### Testing
Tests are available in the `src/test/java` directory.
They are run with [testcontainers](https://www.baeldung.com/spring-boot-testcontainers-integration-test) to ensure that the database is available during the tests.

[testcontainers](https://www.baeldung.com/spring-boot-testcontainers-integration-test) is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases or anything else that can run in a Docker container.

Make sure you have Docker installed and running on your machine before running the tests.

### Logging
Logging is handled by [Log4j2](https://logging.apache.org/log4j/2.x/).
The configuration file is available in the `src/main/resources/log4j2.xml` file.

By default, logs are written to the console. To increase readability, the logs are colored.
Every request is identified by a unique ID that is logged with every log message.
It makes it easier to track the logs of a specific request.

### Error handling
Errors are handled by the `GlobalExceptionHandler` class.
It catches all exceptions and returns a custom error response to the client.
The error response contains the id of the request (see [Logging](#logging)) and the error message.

### FXRatesAPI
The application uses the [FXRatesAPI](https://fxratesapi.com/) to get the exchange rates.
Example can be found on their website.

Here's a sample response for the following request:
```
https://api.fxratesapi.com/latest?currencies=USD&base=EUR&amount=1500
```
```json
{
  "root": {
    "success": true,
    "terms": "https://fxratesapi.com/legal/terms-conditions",
    "privacy": "https://fxratesapi.com/legal/privacy-policy",
    "timestamp": 1739095260,
    "date": "2025-02-09T10:01:00.000Z",
    "base": "EUR",
    "rates": {
      "USD": 1550.1471
    }
  }
}
```

## API
The API documentation is available at [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

The raw JSON documentation is available at [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs)