# Smart Clinic System

A distributed clinic-management backend that coordinates appointment scheduling and financial workflows across two independently deployable Spring Boot services.

The project applies **Domain-Driven Design**, **Hexagonal Architecture**, and **event-driven communication** to model scheduling, invoicing, payment, cancellation, and refund rules without coupling the services through synchronous REST calls.

> **Project status:** Academic prototype. The system demonstrates the domain model and distributed workflow design; additional reliability and security controls would be required before production use.

## Contents

- [System Capabilities](#system-capabilities)
- [Technology Stack](#technology-stack)
- [Repository Structure](#repository-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running with Docker Compose](#running-with-docker-compose)
- [Demo Walkthrough](#demo-walkthrough)
- [Future Improvements](#future-improvements)

## System Capabilities

### Scheduling

- Define effective weekly schedules for doctors.
- Register periods of doctor unavailability.
- Book one-hour appointments in 15-minute time increments.
- Prevent bookings outside working hours, during unavailability, or over an existing appointment.
- Check in patients, complete appointments, and mark no-shows.
- Cancel appointments with an explicit cancellation initiator.
- Reschedule active or system-cancelled appointments.
- Automatically cancel appointments affected by calendar changes.
- Use optimistic locking to detect concurrent modifications.

### Billing

- Maintain a pricing profile for each doctor.
- Issue a draft proforma invoice when an appointment is booked.
- Calculate appointment charges from the doctor's hourly rate and appointment duration.
- Apply time-based pricing policies and discounts.
- Initiate Stripe PaymentIntents and expose the client secret to the caller.
- Finalize payments through verified Stripe webhook events.
- Evaluate refund eligibility and refund amount after cancellation.
- Initiate Stripe refunds and maintain separate payment/refund ledger entries.
- Expire overdue invoices and return timed-out checkouts to draft state.
- Expose query endpoints for invoice and payment status.



## Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| Web API | Spring Web MVC |
| Persistence | Spring Data JPA / Hibernate |
| Databases | PostgreSQL 15 |
| Messaging | RabbitMQ |
| Payment gateway | Stripe Java SDK and Stripe CLI |
| Validation | Jakarta Bean Validation |
| Containers | Docker and Docker Compose |
| Testing | JUnit, Spring Boot Test, H2, and test doubles |
| Build | Maven / Maven Wrapper |

## Repository Structure

```text
smart-clinic-system/
├── billing-service/
│   ├── src/main/java/com/billingcontext/
│   │   ├── application/       # Use cases and ports
│   │   ├── domain/            # Aggregates, policies and value objects
│   │   └── infrastructure/    # REST, RabbitMQ, JPA, Stripe and scheduling adapters
│   ├── src/test/
│   ├── Dockerfile
│   └── pom.xml
├── scheduling-service/
│   ├── src/main/java/com/smartclinicsystem/
│   │   ├── application/       # Commands, use cases and ports
│   │   ├── domain/            # Appointment and calendar domain model
│   │   └── infrastructure/    # REST, RabbitMQ and JPA adapters
│   ├── src/test/
│   ├── Dockerfile
│   └── pom.xml
├── postgres-init/
│   └── init.sql               # Creates one database per bounded context
├── docker-compose.yml
└── README.md
```

## Getting Started

### Prerequisites

For the containerized setup:

- Docker Engine or Docker Desktop
- Docker Compose v2
- A Stripe account with a test-mode secret key

For local development without Docker:

- Java 17
- Maven 3.9+ or the included Maven Wrapper
- PostgreSQL
- RabbitMQ
- Stripe CLI for webhook forwarding

## Configuration

Create a `.env` file in the project root:

```dotenv
STRIPE_API_KEY=sk_test_replace_with_your_test_key
STRIPE_PAYMENT_WEBHOOK_SECRET=whsec_replace_with_your_webhook_secret
```

Use only Stripe **test-mode** credentials for local development and demonstrations.

Do not commit `.env` files or real credentials to source control. A repository intended for publication should contain an `.env.example` file with placeholders instead.

### Payment checkout window

The current Billing configuration uses a short demonstration timeout:

```properties
clinic.checkout.payment-timeout=20s
```

It can be overridden in `docker-compose.yml` under `billing-service.environment`:

```yaml
- CLINIC_CHECKOUT_PAYMENT_TIMEOUT=15s
```

A production-like configuration could use:

```yaml
- CLINIC_CHECKOUT_PAYMENT_TIMEOUT=2h
```

## Running with Docker Compose

From the project root:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up --build -d
```

Check service status:

```bash
docker compose ps
```

Follow logs:

```bash
docker compose logs -f scheduling-service billing-service rabbitmq stripe-cli
```

Stop the system:

```bash
docker compose down
```

Remove containers and database volumes for a clean reset:

```bash
docker compose down -v
```

> Removing the volume permanently deletes the local Scheduling and Billing databases.

### Exposed services

| Component | Address |
|---|---|
| Scheduling API | `http://localhost:8080` |
| Billing API | `http://localhost:8081` |
| RabbitMQ AMQP | `localhost:5672` |
| RabbitMQ Management UI | `http://localhost:15672` |
| PostgreSQL | `localhost:5432` |

RabbitMQ's default local credentials are `guest` / `guest`.

The PostgreSQL container initializes two separate databases:

- `scheduling_db`
- `billing_db`

## Demo Walkthrough

The following sequence demonstrates the main cross-service workflow. Use future dates that fall within the doctor's configured schedule.

### 1. Create a doctor's pricing profile

```bash
curl -X POST http://localhost:8081/api/profile/create \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": "doctor-001",
    "currency": "EUR",
    "hourlyRate": 100.00
  }'
```

### 2. Configure the doctor's weekly schedule

```bash
curl -X POST http://localhost:8080/api/calendars/doctor-001/schedules \
  -H "Content-Type: application/json" \
  -d '{
    "validFrom": "2030-01-01",
    "shifts": {
      "MONDAY": [
        { "startTime": "09:00", "endTime": "17:00" }
      ],
      "TUESDAY": [
        { "startTime": "09:00", "endTime": "17:00" }
      ]
    }
  }'
```

### 3. Book an appointment

Choose a future Monday or Tuesday on or after the schedule's `validFrom` date.

```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": "doctor-001",
    "patientId": "patient-001",
    "appointmentDate": "2030-01-07",
    "startTime": "10:00"
  }'
```

Scheduling publishes `AppointmentWasBooked`. Billing consumes the event and creates a draft invoice asynchronously.

### 4. Find the generated invoice

```bash
curl "http://localhost:8081/api/queries/invoices/latest?status=DRAFT"
```

Copy the returned `invoiceId`.

### 5. Apply pricing rules

```bash
curl -X POST http://localhost:8081/api/payments/{invoiceId}/update/proforma
```

### 6. Initiate Stripe payment

```bash
curl -X POST http://localhost:8081/api/payments/{invoiceId}/initiate/payment
```

The response contains:

- the invoice ID;
- the payment ledger ID;
- the amount in the currency's smallest unit;
- the payment window expiration;
- the Stripe `clientSecret`.

A frontend can use the client secret with Stripe.js. During backend-only demonstrations, Stripe CLI test events can be forwarded to:

```text
http://billing-service:8081/api/stripe/webhook/payments
```

The webhook signing secret configured in Billing must match the secret generated for the active Stripe CLI listener.

### 7. Query invoice or payment state

```bash
curl http://localhost:8081/api/queries/invoices/{invoiceId}/status
```

```bash
curl http://localhost:8081/api/queries/payments/{paymentId}/status
```

### 8. Demonstrate cancellation

```bash
curl -X POST http://localhost:8080/api/appointments/{appointmentId}/cancel \
  -H "Content-Type: application/json" \
  -d '{
    "cancelInitiator": "PATIENT"
  }'
```

Billing consumes the cancellation event and either cancels the invoice or starts the refund process according to the invoice state and refund policies.




## Future Improvements

The most valuable next steps are:

1. Add a transactional outbox to both services.
2. Make all message consumers idempotent.
3. Introduce retry policies and dead-letter queues.
4. Add contract and end-to-end integration tests with RabbitMQ, PostgreSQL, and Stripe test mode.
5. Define correlation IDs and distributed tracing across workflows.
6. Replace `ddl-auto=update` with Flyway or Liquibase migrations.
7. Add API authentication and role-based authorization.
8. Add reconciliation jobs that compare Billing state with Stripe.
9. Document event schemas and introduce explicit event versions.
10. Add OpenAPI documentation and a reusable Postman demonstration collection.

## Academic Purpose

The project was developed to explore how Domain-Driven Design and event-driven microservices can be applied to a domain with strict scheduling invariants and multi-step financial workflows. The main objective is not to claim that microservices are always preferable, but to demonstrate the benefits and costs of bounded-context autonomy, asynchronous communication, and eventual consistency.
"# smart-clinic-system" 
