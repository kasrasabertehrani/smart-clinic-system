# Smart Clinic System

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-brightgreen?logo=springboot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?logo=postgresql&logoColor=white) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?logo=rabbitmq&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)

A clinic-management backend that connects **appointment scheduling** with **invoicing, payments, and refunds** through two independently deployable Spring Boot services.

This project was designed to showcase:

- **Domain-Driven Design** with separate Scheduling and Billing bounded contexts.
- **Hexagonal Architecture** with domain models, application use cases, and infrastructure adapters.
- **Event-driven communication** between services using RabbitMQ.
- **Stripe integration** for payment and refund workflows.
- **Containerized execution** and reproducible API demonstrations with Postman.

> Developed as an academic architecture project. This is an API-only backend; the walkthrough uses Postman and Stripe test mode.

## Demo

The introduction video follows a complete booking-to-payment workflow: configure a doctor's schedule and pricing profile, book an appointment, retrieve its draft invoice, initiate and confirm a Stripe test payment, and verify the final payment and invoice states.

<!-- VIDEO: Replace this comment and the following placeholder with the uploaded GitHub video URL or a link to the hosted introduction video. -->

https://github.com/user-attachments/assets/9acea34d-91cd-4c60-959e-8a51ba7f2bc6


For the complete walkthrough, see the **[Postman demo guide](docs/postman/README.md)**. It includes six collections, a shared local environment template, request descriptions, and saved response examples.

| Collection | Demonstrates |
| --- | --- |
| Booking flow | Doctor schedule and pricing setup, appointment booking, and draft invoice creation |
| Payment flow | Successful and declined Stripe payments, payment records, and invoice state changes |
| Cancellation flow | Patient-initiated cancellation and eligible refunds |
| System cancels appointments | Cancellation after doctor unavailability or working-schedule changes |
| Reschedule appointment | Replacement appointment creation and the original and replacement invoice states |
| Expiration flow | Unpaid invoice expiration and appointment cancellation |

Saved examples can be inspected without running the application.

## Tech Stack

| Area | Technologies |
| --- | --- |
| Application | Java 17, Spring Boot |
| API | Spring Web MVC, Jakarta Bean Validation |
| Architecture | Domain-Driven Design, Hexagonal Architecture |
| Persistence | Spring Data JPA, Hibernate, PostgreSQL 15 |
| Messaging | RabbitMQ |
| Payments | Stripe Java SDK, Stripe CLI |
| Testing | JUnit, Spring Boot Test, H2, test doubles |
| Build | Maven, Maven Wrapper |
| Containers | Docker, Docker Compose |
| API demonstrations | Postman collections and saved examples |

## How to Run

### Docker Compose

Install Git and Docker with Docker Compose v2. Use a Stripe test account for payment and refund demonstrations.

1. Clone the repository:

   ```bash
   git clone https://github.com/kasrasabertehrani/smart-clinic-system.git
   cd smart-clinic-system
   ```

2. Create a `.env` file in the project root:

   ```dotenv
   STRIPE_API_KEY=sk_test_replace_with_your_test_key
   STRIPE_PAYMENT_WEBHOOK_SECRET=whsec_replace_with_your_webhook_secret
   ```

   Use test credentials and keep `.env` out of source control. Billing's webhook signing secret must match the active Stripe CLI listener. When the listener supplies its secret, update `.env` and recreate Billing if necessary to apply the value.

3. Build and start the services:

   ```bash
   docker compose up --build -d
   ```

4. Check service status and logs:

   ```bash
   docker compose ps
   docker compose logs -f scheduling-service billing-service rabbitmq stripe-cli
   ```

### Local Services

| Component | Address |
| --- | --- |
| Scheduling API | `http://localhost:8080` |
| Billing API | `http://localhost:8081` |
| RabbitMQ Management UI | `http://localhost:15672` |
| RabbitMQ AMQP | `localhost:5672` |
| PostgreSQL | `localhost:5432` |

RabbitMQ uses `guest` / `guest` for the local setup. PostgreSQL initializes separate `scheduling_db` and `billing_db` databases.

To stop the system:

```bash
docker compose down
```

To reset it completely, use `docker compose down -v`. This also deletes the local database volumes.

## How to Try the Workflows

1. Import the six collections and environment JSON from [`docs/postman/`](docs/postman/) into Postman.
2. Select the **Smart Clinic System - local** environment and verify the service URLs.
3. Run **Booking flow** to configure a doctor and create an appointment with its draft invoice.
4. Continue with **Payment flow** to initiate payment, confirm the generated PaymentIntent through the documented Stripe CLI command, and query the result.
5. Prepare a fresh appointment for each cancellation, rescheduling, or expiration scenario. Complete payment first when demonstrating a paid scenario.

Run requests manually in the order described by each collection. Update fixed dates to valid future dates, and confirm that “latest” queries select the appointment or invoice you intend to use. Keep the selected identifiers unchanged while checking the outcome.

Stripe CLI must use the same test account or sandbox as Billing. Keep webhook forwarding active so Billing receives the results of the actual PaymentIntent created by your request.

Service events, webhooks, and background jobs are asynchronous. If a query still shows an intermediate state, wait briefly and repeat it.

### Payment Window

Billing uses `clinic.checkout.payment-timeout` to configure the checkout window. The recorded Postman examples use one minute. To reproduce that setting, add this under `billing-service.environment` in `docker-compose.yml`:

```yaml
- CLINIC_CHECKOUT_PAYMENT_TIMEOUT=60s
```

After a declined payment, an invoice can remain `PAYMENT_PENDING` while its checkout window is active. Once the window expires and background processing runs, it returns to `DRAFT`. The effective timeout depends on application configuration and environment overrides.

“Draft invoice” and “proforma invoice” refer to the same stage in this project, represented by `invoiceStatus: DRAFT`.

## Architecture & Design Patterns

The system combines **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports and Adapters)** to organize scheduling and financial rules into separate bounded contexts.

### Domain-Driven Design

**Scheduling** owns doctor calendars and the appointment lifecycle. Its domain rules cover working hours, unavailability, overlapping bookings, cancellation, and rescheduling. Appointments last one hour and can begin at 15-minute intervals. The service also supports check-in, completion, and no-show handling.

**Billing** owns pricing profiles, invoices, and payment/refund records. It calculates charges, applies pricing policies, manages checkout windows, and evaluates cancellation and refund outcomes.

Aggregates, value objects, and domain policies express these rules. Each service maintains its own database, allowing Scheduling and Billing to evolve around their respective responsibilities.

### Hexagonal Architecture

Both services organize their implementation into three areas:

| Layer | Responsibility |
| --- | --- |
| `domain` | Business models, invariants, policies, and domain events |
| `application` | Use cases, workflow coordination, and ports |
| `infrastructure` | REST endpoints, persistence, messaging, and external-service adapters |

This structure separates business decisions from the mechanisms used to receive requests, persist data, exchange messages, and communicate with Stripe.

### Event-Driven Workflows

Scheduling and Billing coordinate through RabbitMQ rather than synchronous REST calls between the services. Booking an appointment publishes an event that Billing consumes to create its invoice. Cancellation events trigger the corresponding billing workflow, while Stripe webhooks report payment and refund outcomes.

Each service commits its own changes, so related states become consistent asynchronously. The Postman scenarios make these transitions observable through status queries.

## Project Structure

| Path | Contents |
| --- | --- |
| [`scheduling-service/`](scheduling-service/) | Appointment and calendar domain, application use cases, and adapters |
| [`billing-service/`](billing-service/) | Pricing, invoices, payments, refunds, and adapters |
| [`docs/postman/`](docs/postman/) | Demo guide, six collections, and local environment template |
| [`postgres-init/`](postgres-init/) | Initialization script for the two service databases |
| [`docker-compose.yml`](docker-compose.yml) | Local service and infrastructure configuration |

For a more detailed explanation of the system’s architecture and design decisions, see [report_SAP.pdf](report_SAP.pdf) in this repository.

## Project Scope & Next Steps

This academic prototype explores bounded-context autonomy, business-rule modeling, and eventual consistency across a financial workflow. The included tests and saved API examples support development and demonstration; saved examples are snapshots of previous runs.

Further work before production use includes:

- Reliable event delivery through transactional outboxes, consumer idempotency, retries, and dead-letter queues.
- API authentication and role-based authorization.
- Database migrations and versioned event contracts.
- End-to-end integration tests, distributed tracing, and Stripe reconciliation.
- OpenAPI documentation for both services.
