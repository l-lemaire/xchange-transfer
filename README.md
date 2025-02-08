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