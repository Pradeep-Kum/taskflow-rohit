# TaskFlow

TaskFlow is a backend-focused task management API for users, projects, and tasks. It includes registration/login with JWTs, PostgreSQL persistence, Flyway migrations, seeded reviewer data, and Docker Compose for one-command local startup.

This submission is backend-only. The frontend is intentionally not included.

## Architecture Decisions

- Kotlin + Ktor is used for the API server, with feature-oriented modules for auth, projects, and tasks.
- Each feature is split into domain, service, and data layers to keep business rules separate from HTTP and database code.
- PostgreSQL is accessed through Exposed SQL DSL, but schema management is handled by Flyway migrations rather than auto-migration.
- JWT authentication is configured at the Ktor routing layer. Public routes live under `/auth`; project and task routes require `Authorization: Bearer <token>`.
- Passwords are hashed with bcrypt cost 12.
- Docker Compose starts PostgreSQL and the API. Flyway migrations run automatically when the API starts.
- I kept the scope backend-only and left frontend work out intentionally.

## Running Locally

Assuming Docker is installed:

```bash
git clone https://github.com/your-name/taskflow-rohit
cd taskflow-rohit
cp .env.example .env
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

## Running Migrations

Migrations run automatically when the API container starts.

Up migrations are in:

```text
app/src/main/resources/db/migration
```

Down migration SQL is included for review/reference in:

```text
app/src/main/resources/db/down
```

## Test Credentials

```text
Email:    test@example.com
Password: password123
```

## API Reference

### Auth

`POST /auth/register`

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "secret123"
}
```

`POST /auth/login`

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "<jwt>",
  "user": {
    "id": "11111111-1111-1111-1111-111111111111",
    "name": "Test User",
    "email": "test@example.com"
  }
}
```

Use the token for all non-auth endpoints:

```text
Authorization: Bearer <jwt>
```

### Projects

`GET /projects`

Lists projects owned by the current user or projects where the current user has assigned tasks.

`POST /projects`

```json
{
  "name": "New Project",
  "description": "Optional description"
}
```

`GET /projects/{id}`

Returns project details with tasks.

`PATCH /projects/{id}`

```json
{
  "name": "Updated Name",
  "description": "Updated description"
}
```

`DELETE /projects/{id}`

Deletes a project and its tasks. Owner only.

### Tasks

`GET /projects/{id}/tasks?status=todo&assignee=11111111-1111-1111-1111-111111111111`

Lists tasks for a project with optional filters.

`POST /projects/{id}/tasks`

```json
{
  "title": "Design homepage",
  "description": "Create the first pass",
  "priority": "high",
  "assignee_id": "11111111-1111-1111-1111-111111111111",
  "due_date": "2026-04-15"
}
```

`PATCH /tasks/{taskId}`

```json
{
  "title": "Updated title",
  "status": "done",
  "priority": "low",
  "assignee_id": "11111111-1111-1111-1111-111111111111",
  "due_date": "2026-04-20"
}
```

`DELETE /tasks/{taskId}`

Deletes a task.

### Errors

```json
{ "error": "unauthorized" }
```

```json
{ "error": "forbidden" }
```

```json
{ "error": "not found" }
```

```json
{ "error": "validation failed", "fields": { "email": "is required" } }
```

## What I'd Do With More Time

- Add integration tests for auth, project ownership, and task filtering.
- Add stricter request validation with consistent field-level error responses everywhere.
- Add pagination to project and task list endpoints.
- Add a frontend only if the role/submission requires full stack.
