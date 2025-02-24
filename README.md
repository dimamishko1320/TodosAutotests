# TODO API Integration Tests

This repository contains integration tests for testing the TODO API. The tests cover the basic CRUD operations for managing TODO items, such as creating, retrieving, updating, and deleting TODOs.

## Project Structure

- **`src/main/java`**: Contains the main application code.
  - **`client`**: API client for making requests to the TODO API.
  - **`dataService`**: Handles interaction with the database and managing TODO entities.

- **`src/test/java`**: Contains the test code.
  - **`todo`**: Test classes for the TODO API's CRUD operations (GET, POST, PUT, DELETE).

## Prerequisites

Before running the tests, ensure you have the following installed:

- Java 17 or later
- Maven
- Docker (optional, if running services locally)

## Running the Tests

Follow these steps to clone the repository and run the tests:

1. **Clone the repository**:

    ```bash
    git clone <repository_url>
    cd <project_directory>
    ```

2. **Install dependencies and build the project**:

    ```bash
    mvn clean install
    ```

3. **Run the tests**:

    ```bash
    mvn test
    ```

4. **Check the test results with Allure report**:

   After the tests run, generate and view the Allure report:

    ```bash
    mvn allure:serve
    ```