# Software Mind Recruitment Task

## Description

This application is exposes REST API to manage reservations for tables. It allows creation and edition of tables and
reservations assigned to tables.

## Usage

### Requirements

Requires Java 25.

### Database Configuration

By default, the application uses in file H2 database:

```
spring.datasource.url=jdbc:h2:file:./db/database
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

There is separate configuration for prod and dev profiles (`application.properties` and `application-dev.properties`
files respectively).

### Starting

* Gradle

  You can use gradle script to start the application.
    - In Windows: `gradlew.bat bootRun`
    - In Linux: `.\gradlew bootRun`

      You can add `--args='--spring.profiles.active=dev'` to command to start application in dev profile. This will
      initialise database with examples and allow access to h2-console.

      By default, application starts on `localhost:8080`.

* Docker

  Docker file is provided, allowing to build docker image.

    - To build image use command `docker build -t softwaremind .` in location of dockerfile.
    - To start use command `docker run -d -p 8080:8080 -t softwaremind`
    - To start in dev mode use command `docker run -e spring_profiles_active=dev -d -p 8080:8080 -t softwaremind`
      
      This will initialise database with examples and allow access to h2-console.

### Access

For demo purpose two user are defined:

-   username: 'admin'; password: 'password'
-   username: 'user'; password: 'password'

Authorization uses Basic auth. Only admin can create, modify or delete tables.

### Swagger

[Swagger](http://localhost:8080/swagger-ui/index.html) is enabled, allowing to inspect and use REST API without need of external tools, like postman.

