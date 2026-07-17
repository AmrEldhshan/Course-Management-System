# Course Management System (CMS) v2.0

A multi-service course management system split into two independent Spring Boot
applications (`admin` and `public`), backed by a shared PostgreSQL database,
fully containerized with Docker Compose.

## Architecture

- **admin** (port 8080) — course creation and management (CRUD)
- **public** (port 8082) — student-facing course browsing and time-bound enrollment
- **db** — PostgreSQL 16, shared by both services

Both services connect to the same `cms_db` database and share the `courses` table
schema. `admin` owns course creation; `public` owns enrollments.

## Prerequisites

- Docker Desktop (with Docker Compose)
- No local Java/Maven install needed to *run* the system — everything builds inside Docker
- Java 17 + Maven only needed if you want to run/test services outside Docker

## Running the whole system

From the project root:

```bash
docker-compose up --build
```

This builds both service images, starts Postgres, waits for it to be healthy, then
starts `admin` and `public`. On first run (fresh database), sample data is
automatically loaded — see [Data Seeding](#data-seeding) below.

Once running:
- Admin API: `http://localhost:8080/api/admin/courses`
- Public API: `http://localhost:8082/api/public/courses`
- Postgres: `localhost:5432` (user: `cms_user`, password: `cms_pass`, db: `cms_db`)

To stop:
```bash
docker-compose down
```

To stop **and wipe the database** (useful to reset seed data):
```bash
docker-compose down -v
```

## Data Seeding

On first container startup (empty database volume only), `seed/01-seed.sql` runs
automatically and creates 5 sample courses covering every enrollment scenario:
- 3 courses currently open for registration
- 1 course with registration not yet started
- 1 course with registration already closed

This only runs once per fresh volume. If you've already run the system before and
want the seed data back, reset with `docker-compose down -v` first.

## API Reference

### Admin service (`localhost:8080`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/courses` | List all courses |
| GET | `/api/admin/courses/{id}` | Get a course by ID |
| POST | `/api/admin/courses` | Create a course |

**Create course example:**
```json
POST /api/admin/courses
{
  "title": "Intro to Spring Boot",
  "description": "Learn Spring Boot basics",
  "instructorName": "Dr. Ahmed",
  "registrationStartTime": "2026-07-01T00:00:00",
  "registrationEndTime": "2026-08-01T00:00:00"
}
```

### Public service (`localhost:8082`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/public/courses` | List all courses |
| GET | `/api/public/courses/{id}` | Get a course by ID |
| POST | `/api/public/enrollments` | Enroll in a course (time-window validated) |

**Enroll example:**
```json
POST /api/public/enrollments
{
  "courseId": 1,
  "studentName": "Amr"
}
```

If the current time is outside the course's registration window, this returns
`400 Bad Request` with a descriptive message, e.g.:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Registration period has ended. It closed at 2026-08-01T00:00"
}
```

## Running Unit Tests

Tests use JUnit 5 and Mockito, and don't require Docker or a database —
repositories are mocked.

```bash
cd admin
mvn test

cd ../public
mvn test
```

## Running a service locally without Docker (development)

Not recommended for normal use (Docker Compose is the intended way to run this
project), but useful during development:

1. Start only Postgres via Docker: `docker-compose up db`
2. Update the service's `application.properties` datasource URL from `db` to `localhost`
3. Run: `mvn spring-boot:run`

Remember to revert the datasource URL back to `db` before committing, since that's
required for the Docker Compose setup to work.

## Project Structure

```
Course-Management-System/
├── docker-compose.yml
├── seed/
│   └── 01-seed.sql
├── admin/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/cms/admin/
│       ├── CourseManagementAdminApplication.java
│       ├── controller/
│       ├── service/
│       ├── serviceImpl/
│       ├── repository/
│       ├── entity/
│       └── dto/
└── public/
    ├── Dockerfile
    ├── pom.xml
    └── src/main/java/com/cms/publicapp/
        ├── CourseManagementPublicApplication.java
        ├── controller/
        ├── service/
        ├── serviceImpl/
        ├── repository/
        └── entity/
```

Note: the `public` folder's Java package is `com.cms.publicapp`, not `com.cms.public`,
since `public` is a reserved keyword in Java and cannot be used as a package name.

## Tech Stack

- Java 17
- Spring Boot 3.3.4 (Web, Data JPA, Validation)
- PostgreSQL 16
- Maven
- Docker / Docker Compose
- JUnit 5 + Mockito