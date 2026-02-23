# User Management Service

A RESTful API service for user management built with Spring Boot 3.5.10. This project implements a complete user management system with JWT authentication, asynchronous event processing, and comprehensive API documentation via Swagger.

---

## About This Project

This is a Spring Boot REST API that provides user management functionality with the following features:

- Complete CRUD operations for users (Create, Read, Update, Delete)
- Multiple role assignment per user (OWNER, OPERATOR, MAINTAINER, DEVELOPER, REPORTER)
- Email uniqueness validation and immutability
- JWT-based authentication using OAuth2 Resource Server with Keycloak
- Asynchronous event system that triggers when users are created
- Interactive API documentation with Swagger UI
- Global exception handling with meaningful error messages
- H2 in-memory database (can be easily switched to PostgreSQL or MySQL)
- Comprehensive test suite including unit tests, integration tests, and async tests

---

## Technology Stack

This project uses the following technologies:

**Core Framework:**
- Java 17 (Long Term Support version)
- Spring Boot 3.5.10
- Maven 3.9.x for build management

**Data Layer:**
- Spring Data JPA for database operations
- Hibernate 6.6.x as ORM framework
- H2 Database 2.3.x for in-memory storage (development)

**Security:**
- Spring Security
- OAuth2 Resource Server for JWT token validation
- Keycloak integration for authentication

**API Documentation:**
- SpringDoc OpenAPI 2.8.0
- Swagger UI for interactive API testing

**Testing:**
- JUnit 5 for test framework
- Mockito for mocking dependencies
- AssertJ for fluent assertions
- Awaitility for testing asynchronous operations

**Utilities:**
- Lombok for reducing boilerplate code

---

## Prerequisites

Before you can run this project, you need to have the following software installed on your computer:

**Required Software:**

1. **Java Development Kit (JDK) 17 or higher**
   - Download from: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - Or use OpenJDK: https://adoptium.net/

2. **Apache Maven 3.9.x or higher**
   - Download from: https://maven.apache.org/download.cgi
   - The project includes Maven Wrapper (mvnw), so Maven installation is optional

3. **Git**
   - Download from: https://git-scm.com/downloads

**Optional but Recommended:**

- An IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code
- Postman or similar tool for API testing (though Swagger UI is included)

**Verify Your Installation:**

Open a terminal or command prompt and run these commands:

```bash
java -version
```
You should see Java version 17.x.x or higher

```bash
mvn -version
```
You should see Apache Maven 3.9.x or higher

```bash
git --version
```
You should see git version 2.x.x or higher

---

## Getting the Project

To download the project from GitHub, open a terminal and run:

```bash
git clone https://github.com/HamidCodeHub/user-management-service.git
```

This will create a folder called "user-management-service" with all the project files.

Navigate into the project folder:

```bash
cd user-management-service
```

---

## Building the Project

Before running the application, you need to build it. This will download all necessary dependencies and compile the code.

**On Windows:**

```bash
mvnw.cmd clean install
```

**On Linux/Mac:**

```bash
./mvnw clean install
```

This command will:
- Download all required libraries
- Compile the source code
- Run all tests
- Package the application as a JAR file

The build process might take a few minutes the first time as it downloads dependencies. When it completes successfully, you'll see:

```
[INFO] BUILD SUCCESS
[INFO] Total time: XX s
```

If you want to skip the tests during build, you can run:

```bash
mvnw clean install -DskipTests
```

---

## Running the Application

There are several ways to run the application:

**Method 1: Using Maven (Recommended for Development)**

```bash
mvnw spring-boot:run
```

**Method 2: Using the JAR file**

First, build the project (if you haven't already):

```bash
mvnw clean package
```

Then run the generated JAR file:

```bash
java -jar target/user-management-service-0.0.1-SNAPSHOT.jar
```

**Method 3: From Your IDE**

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Find the main class: `src/main/java/com/hamid/usermanagement/UserManagementApplication.java`
3. Right-click on the file and select "Run" or "Run As → Java Application"

**Confirmation:**

When the application starts successfully, you will see output similar to this in the console:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.5.10)

...
Started UserManagementApplication in 3.456 seconds
```

The application is now running and accessible at `http://localhost:8080`

---

## Using the API Documentation (Swagger UI)

The easiest way to explore and test the API is through Swagger UI, which provides an interactive interface.

**Accessing Swagger UI:**

Once the application is running, open your web browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

You will see an interactive API documentation page with all available endpoints.

**How to Use Swagger UI:**

1. **Viewing Endpoints:**
   - All API endpoints are listed with their HTTP methods (GET, POST, PUT, DELETE)
   - Each endpoint shows what it does, what parameters it accepts, and what it returns

2. **Testing an Endpoint:**
   - Click on any endpoint to expand it
   - Click the "Try it out" button
   - Fill in the required parameters (Swagger provides example values)
   - Click "Execute"
   - See the response below, including status code and returned data

**Example: Creating a New User**

1. Find the endpoint: `POST /api/v1/users`
2. Click to expand it
3. Click "Try it out"
4. You'll see a JSON example like this:

```json
{
  "username": "john.doe",
  "email": "john.doe@example.com",
  "taxCode": "JHNDOE90A01H501Z",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["DEVELOPER"]
}
```

5. Modify the values if you want
6. Click "Execute"
7. Scroll down to see the response

You should get a response with status code 201 (Created) and the created user with an ID:

```json
{
  "id": 1,
  "username": "john.doe",
  "email": "john.doe@example.com",
  "taxCode": "JHNDOE90A01H501Z",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["DEVELOPER"]
}
```

---

## Available API Endpoints

The application provides the following REST endpoints:

**Base URL:** `http://localhost:8080`

**User Management Endpoints:**

1. **GET /api/v1/users**
   - Description: Retrieve all users
   - Returns: Array of user objects
   - Example: `http://localhost:8080/api/v1/users`

2. **GET /api/v1/users/{id}**
   - Description: Get a specific user by ID
   - Parameters: id (user ID, e.g., 1)
   - Returns: Single user object
   - Example: `http://localhost:8080/api/v1/users/1`

3. **POST /api/v1/users**
   - Description: Create a new user
   - Requires: JSON body with user details
   - Returns: Created user with assigned ID
   - Status Code: 201 Created

4. **PUT /api/v1/users/{id}**
   - Description: Update an existing user
   - Parameters: id (user ID to update)
   - Requires: JSON body with updated user details
   - Returns: Updated user object
   - Note: Email cannot be changed (immutable)

5. **DELETE /api/v1/users/{id}**
   - Description: Delete a user
   - Parameters: id (user ID to delete)
   - Returns: No content
   - Status Code: 204 No Content

---

## User Data Model

**User Object Structure:**

```json
{
  "id": 1,
  "username": "john.doe",
  "email": "john.doe@example.com",
  "taxCode": "JHNDOE90A01H501Z",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["DEVELOPER", "OPERATOR"]
}
```

**Field Descriptions:**

- `id`: Unique identifier (automatically generated)
- `username`: User's login name (required, unique, changeable)
- `email`: User's email address (required, unique, immutable - cannot be changed after creation)
- `taxCode`: Tax code or fiscal code (required, changeable)
- `firstName`: User's first name (required, changeable)
- `lastName`: User's last name (required, changeable)
- `roles`: Array of roles assigned to the user (required, changeable)

**Available Roles:**

- `OWNER`: Full ownership privileges
- `OPERATOR`: Operational access
- `MAINTAINER`: Maintenance privileges
- `DEVELOPER`: Development access
- `REPORTER`: Reporting and viewing access

A user can have multiple roles simultaneously.

**Validation Rules:**

- Username must not be blank and must be unique
- Email must be valid format and unique (cannot be changed after creation)
- Tax code must not be blank
- First name must not be blank
- Last name must not be blank
- At least one role must be assigned

---

## Testing the API with Examples

Here are some practical examples you can try in Swagger UI:

**Example 1: Create a Developer User**

Navigate to `POST /api/v1/users` in Swagger UI, click "Try it out", and use:

```json
{
  "username": "alice.johnson",
  "email": "alice.johnson@example.com",
  "taxCode": "ALCJHN90A01H501Z",
  "firstName": "Alice",
  "lastName": "Johnson",
  "roles": ["DEVELOPER"]
}
```

**Example 2: Create a User with Multiple Roles**

```json
{
  "username": "bob.smith",
  "email": "bob.smith@example.com",
  "taxCode": "BOBSMT85M01H501Z",
  "firstName": "Bob",
  "lastName": "Smith",
  "roles": ["DEVELOPER", "OPERATOR", "MAINTAINER"]
}
```

**Example 3: Update a User**

First, create a user and note its ID (e.g., ID = 1). Then use `PUT /api/v1/users/1`:

```json
{
  "username": "alice.johnson.updated",
  "taxCode": "ALCJHN90A01H501Z",
  "firstName": "Alice",
  "lastName": "Johnson-Updated",
  "roles": ["DEVELOPER", "OWNER"]
}
```

Note: You cannot change the email in an update request.

**Example 4: Get All Users**

Navigate to `GET /api/v1/users` in Swagger UI and click "Execute". You'll see all created users.

**Example 5: Delete a User**

Navigate to `DELETE /api/v1/users/{id}`, enter an ID (e.g., 1), and click "Execute". The user will be deleted.

---

## Accessing the Database Console

The application uses H2, an in-memory database. You can view and query the database directly through the H2 console.

**Accessing H2 Console:**

1. Make sure the application is running
2. Open your browser and go to: `http://localhost:8080/h2-console`
3. You'll see a login page

**Login Credentials:**

- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (leave empty)

Click "Connect"

**Exploring the Database:**

Once connected, you'll see the database schema on the left side with two tables:

- `USERS`: Contains user information
- `USER_ROLES`: Contains the many-to-many relationship between users and roles

You can write SQL queries in the text area to explore the data:

```sql
-- View all users
SELECT * FROM USERS;

-- View user roles
SELECT * FROM USER_ROLES;

-- Join query to see users with their roles
SELECT u.USERNAME, u.EMAIL, ur.ROLE 
FROM USERS u 
LEFT JOIN USER_ROLES ur ON u.ID = ur.USER_ID;
```

---

## Understanding Asynchronous Events

This application implements an event-driven architecture. When a user is created, the system publishes an event that is processed asynchronously in the background.

**How It Works:**

When you create a user via `POST /api/v1/users`:

1. The user is saved to the database
2. A `UserCreatedEvent` is published
3. The API immediately returns the response to you (very fast, ~100ms)
4. In the background, a separate thread processes the event (logs details, could send emails, update statistics, etc.)

**Why This Matters:**

- **Faster Response**: You get the response immediately without waiting for background tasks
- **Better Performance**: The server can handle more requests per second
- **Scalability**: Background tasks don't block the main request handling
- **Resilience**: If background tasks fail, the user is still created successfully

**Seeing It In Action:**

Watch the application console logs when you create a user. You'll see something like:

```
[http-nio-8080-exec-1] INFO : Creating new user with username: alice.johnson
[http-nio-8080-exec-1] INFO : Publishing UserCreatedEvent for user: alice.johnson
[http-nio-8080-exec-1] INFO : User created successfully with id: 1

[task-1] INFO : ┌─────────────────────────────────────────┐
[task-1] INFO : │      USER CREATED EVENT RECEIVED        │
[task-1] INFO : │ User ID: 1                              │
[task-1] INFO : │ Username: alice.johnson                 │
[task-1] INFO : └─────────────────────────────────────────┘
[task-1] INFO : 🔄 Starting async processing for user: alice.johnson
[task-1] INFO : ✅ Async processing completed for user: alice.johnson
```

Notice the different thread names:
- `[http-nio-8080-exec-1]`: Main HTTP request thread (fast)
- `[task-1]`: Asynchronous background thread (processes event after response is sent)

---

## Running the Tests

The project includes comprehensive tests covering all layers of the application.

**Run All Tests:**

```bash
mvnw test
```

This will run:
- Controller tests (testing REST endpoints)
- Service tests (testing business logic)
- Repository tests (testing database operations)
- Event listener tests (testing async event processing)
- Exception handler tests (testing error handling)
- Integration tests (testing complete workflows)

**Run a Specific Test Class:**

```bash
mvnw test -Dtest=UserControllerTest
mvnw test -Dtest=UserServiceImplTest
mvnw test -Dtest=UserRepositoryTest
```

**Understanding Test Results:**

After running tests, you'll see a summary:

```
Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
```

- Tests run: Total number of test methods executed
- Failures: Tests that failed (should be 0)
- Errors: Tests that encountered errors (should be 0)
- Skipped: Tests that were skipped

All tests should pass successfully.

---

## Authentication and Security

The application is configured to support JWT-based authentication using OAuth2 Resource Server with Keycloak integration.

**Current Configuration (Development Mode):**

For ease of testing and development, authentication is currently **disabled by default**. This means you can test all API endpoints through Swagger UI without needing a JWT token.

**Enabling Authentication:**

To enable JWT authentication:

1. Uncomment or configure the authentication settings in `SecurityConfig.java`
2. Configure your Keycloak server details in `application.yml`
3. Obtain a JWT token from your Keycloak server
4. Use the token in Swagger UI by clicking the "Authorize" button and pasting your token

**Using Authentication in Swagger UI:**

When authentication is enabled:

1. Obtain a JWT token from Keycloak
2. In Swagger UI, click the "Authorize" button (lock icon at the top)
3. Paste your JWT token in the input field
4. Click "Authorize" and then "Close"
5. All subsequent requests will include the authentication token

---

## Configuration Files

**Application Configuration: application.yml**

The application is configured via `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: user-management-service
  
  datasource:
    url: jdbc:h2:mem:userdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080

springdoc:
  api-docs:
    path: /api-docs
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
    operationsSorter: method
    tagsSorter: alpha
    tryItOutEnabled: true
```

**Configuration Explanation:**

- `spring.application.name`: Application name identifier
- `spring.datasource.*`: H2 database connection settings
- `spring.jpa.hibernate.ddl-auto: create-drop`: Database schema is created on startup and dropped on shutdown
- `spring.jpa.show-sql: true`: Shows SQL queries in console (useful for debugging)
- `spring.h2.console.enabled: true`: Enables H2 database console
- `server.port: 8080`: Application runs on port 8080
- `springdoc.*`: Swagger/OpenAPI documentation configuration

**Changing the Port:**

If port 8080 is already in use, you can change it:

```yaml
server:
  port: 8081
```

Then access the application at `http://localhost:8081`

---

## Project Structure

```
user-management-service/
│
├── src/
│   ├── main/
│   │   ├── java/com/hamid/usermanagement/
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── AsyncConfig.java          (Enables async processing)
│   │   │   │   ├── OpenApiConfig.java        (Swagger configuration)
│   │   │   │   └── SecurityConfig.java       (Security/JWT configuration)
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── UserController.java       (REST API endpoints)
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── CreateUserRequest.java
│   │   │   │   │   └── UpdateUserRequest.java
│   │   │   │   └── response/
│   │   │   │       └── UserResponse.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── Role.java                 (Role enum)
│   │   │   │   └── User.java                 (User entity/table)
│   │   │   │
│   │   │   ├── event/
│   │   │   │   ├── UserCreatedEvent.java     (Event object)
│   │   │   │   └── UserCreatedEventListener.java (Event handler)
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── EmailAlreadyExistsException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── UserNotFoundException.java
│   │   │   │
│   │   │   ├── mapper/
│   │   │   │   └── UserMapper.java           (Entity <-> DTO conversion)
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java       (Database access)
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── UserService.java          (Business logic interface)
│   │   │   │   └── UserServiceImpl.java      (Business logic implementation)
│   │   │   │
│   │   │   └── UserManagementApplication.java (Main application class)
│   │   │
│   │   └── resources/
│   │       └── application.yml               (Application configuration)
│   │
│   └── test/
│       └── java/com/hamid/usermanagement/
│           ├── controller/
│           │   └── UserControllerTest.java
│           ├── service/
│           │   └── UserServiceImplTest.java
│           ├── repository/
│           │   └── UserRepositoryTest.java
│           ├── event/
│           │   └── UserCreatedEventListenerTest.java
│           ├── exception/
│           │   └── GlobalExceptionHandlerTest.java
│           ├── mapper/
│           │   └── UserMapperTest.java
│           └── integration/
│               └── UserIntegrationTest.java
│
├── target/                                   (Generated files, JAR output)
├── .gitignore
├── mvnw                                      (Maven wrapper for Unix/Mac)
├── mvnw.cmd                                  (Maven wrapper for Windows)
├── pom.xml                                   (Maven configuration/dependencies)
└── README.md                                 (This file)
```

---

## Common Issues and Solutions

**Problem: Port 8080 is already in use**

Error message:
```
Web server failed to start. Port 8080 was already in use.
```

Solution 1: Kill the process using port 8080

On Windows:
```bash
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

On Linux/Mac:
```bash
lsof -i :8080
kill -9 <PID_NUMBER>
```

Solution 2: Change the port in application.yml:
```yaml
server:
  port: 8081
```

**Problem: Java version mismatch**

Error message:
```
Unsupported class file major version XX
```

Solution: Ensure Java 17 or higher is installed and set in your PATH:
```bash
java -version
```

**Problem: Maven build fails**

Solution: Clean the project and rebuild:
```bash
mvnw clean install -U
```

The `-U` flag forces Maven to update all dependencies.

**Problem: Tests fail**

Solution: Run tests with verbose output to see details:
```bash
mvnw clean test -X
```

Review the error messages and stack traces to identify the issue.

**Problem: Cannot access Swagger UI**

Solution: 
- Verify the application is running (check console for "Started UserManagementApplication")
- Ensure you're using the correct URL: `http://localhost:8080/swagger-ui/index.html`
- Check that port 8080 is not blocked by firewall
- Try clearing browser cache

**Problem: H2 Console not accessible**

Solution:
- Verify application is running
- Check that H2 console is enabled in application.yml:
```yaml
spring:
  h2:
    console:
      enabled: true
```
- Use correct URL: `http://localhost:8080/h2-console`
- Use correct JDBC URL: `jdbc:h2:mem:userdb`

---

## Stopping the Application

**If running with Maven:**
Press `Ctrl+C` in the terminal where the application is running

**If running as JAR:**
Press `Ctrl+C` in the terminal

**If running from IDE:**
Click the stop button in your IDE

The application will shut down gracefully, closing database connections and cleaning up resources.

---

## Next Steps and Enhancements

This application can be extended with additional features:

**Potential Enhancements:**

1. **Email Integration**: Actually send welcome emails when users are created
2. **Role-Based Access Control**: Implement fine-grained permissions based on roles
3. **Field Filtering**: Show/hide fields based on user's role
4. **Password Management**: Add password fields with encryption
5. **User Profile Pictures**: Support uploading and storing user avatars
6. **Audit Trail**: Track who created/modified each user and when
7. **Pagination**: Add pagination to the GET all users endpoint
8. **Search and Filter**: Add search by username, email, or role
9. **Production Database**: Switch from H2 to PostgreSQL or MySQL
10. **Docker Support**: Add Dockerfile and docker-compose for containerization

---

## Additional Resources

**Official Documentation:**

- Spring Boot Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
- Spring Data JPA Documentation: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- Spring Security Documentation: https://docs.spring.io/spring-security/reference/index.html
- SpringDoc OpenAPI Documentation: https://springdoc.org/
- H2 Database Documentation: http://www.h2database.com/html/main.html

**Learning Resources:**

- Spring Boot Getting Started Guide: https://spring.io/guides/gs/spring-boot/
- Building a RESTful Web Service: https://spring.io/guides/gs/rest-service/
- Spring Boot Testing: https://spring.io/guides/gs/testing-web/

---

## Support and Contact

**Repository:**
- GitHub: https://github.com/HamidCodeHub/user-management-service

**Issues:**
If you encounter any problems or have questions:
1. Check this README thoroughly
2. Review the "Common Issues and Solutions" section
3. Check existing GitHub issues
4. Create a new issue with:
   - Description of the problem
   - Steps to reproduce
   - Error messages (if any)
   - Your environment (OS, Java version, etc.)

---

## License

This project is open source and available under the MIT License.

---

## Author

**Hamid**

GitHub: [@HamidCodeHub](https://github.com/HamidCodeHub)

---

**Last Updated:** February 2026

---

## Quick Reference

**Important URLs (when application is running):**

- Application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- API Docs JSON: http://localhost:8080/api-docs
- H2 Console: http://localhost:8080/h2-console

**Quick Commands:**

```bash
# Clone project
git clone https://github.com/HamidCodeHub/user-management-service.git
cd user-management-service

# Build project
mvnw clean install

# Run application
mvnw spring-boot:run

# Run tests
mvnw test

# Build JAR
mvnw clean package

# Run JAR
java -jar target/user-management-service-0.0.1-SNAPSHOT.jar
```

**H2 Database Login:**
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (empty)

# Docker Deployment Section (Add this to README.md)

Add this section after the "Running the Application" section in your main README.md:

---

## Running with Docker

The application can be easily run using Docker, which eliminates the need to install Java and Maven on your machine.

### Prerequisites for Docker

You only need Docker installed:

- **Docker Desktop** (Windows/Mac): https://www.docker.com/products/docker-desktop
- **Docker Engine** (Linux): https://docs.docker.com/engine/install/

Verify installation:
```bash
docker --version
docker-compose --version
```

### Quick Start with Docker Compose

This is the easiest way to run the application:

```bash
# 1. Clone the repository
git clone https://github.com/HamidCodeHub/user-management-service.git
cd user-management-service

# 2. Start the application
docker-compose up -d

# 3. Access Swagger UI
# Open browser: http://localhost:8080/swagger-ui/index.html
```

That's it! The application is now running in a Docker container.

### Viewing Logs

```bash
# View application logs
docker-compose logs -f user-management-service
```

### Stopping the Application

```bash
# Stop the container
docker-compose down
```

### Building the Docker Image Manually

If you prefer to build and run manually:

```bash
# Build the image
docker build -t user-management-service:1.0.0 .

# Run the container
docker run -d -p 8080:8080 --name user-management-service user-management-service:1.0.0

# View logs
docker logs -f user-management-service

# Stop and remove
docker rm -f user-management-service
```

### Docker Image Details

The Docker image includes:
- ✅ Multi-stage build for optimized size (~250MB)
- ✅ Alpine Linux base for security
- ✅ Non-root user for better security
- ✅ Built-in health checks
- ✅ Optimized JVM settings for containers

### Complete Docker Documentation

For detailed Docker instructions including:
- PostgreSQL integration
- Production deployment tips
- Troubleshooting guide
- Docker commands reference

See the complete guide: **[DOCKER.md](DOCKER.md)**

---

## Docker vs Traditional Deployment

**Running with Docker:**
```bash
docker-compose up -d
```
✅ No Java installation needed
✅ No Maven installation needed
✅ Consistent environment
✅ Easy to deploy anywhere
✅ Simple to start/stop

**Running Traditionally:**
```bash
mvnw spring-boot:run
```
✅ Faster startup (no container overhead)
✅ Direct access to source code
✅ Easier debugging in IDE
✅ Better for active development

**Recommendation:**
- **Development**: Use Maven directly for faster iteration
- **Testing/Demo**: Use Docker for consistent environment
- **Production**: Use Docker for reliable deployment
