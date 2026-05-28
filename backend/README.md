# Loan Management API (Backend)

API REST para gestion de prestamos bancarios construida con Spring Boot 3 y arquitectura limpia/hexagonal en capas: dominio, aplicacion, infraestructura e interfaces REST.

## Stack

- Java 17
- Spring Boot 3.x
- Maven
- Spring Web, Spring Data JPA, Spring Security
- JWT (`jjwt`)
- Hibernate Validator
- H2 en memoria
- Lombok
- Spring Cache + Caffeine
- JUnit 5 + Mockito + MockMvc

## Arquitectura

Estructura principal:

- `domain`: modelos puros, enums, puertos de repositorio, excepciones de negocio.
- `application`: servicios/casos de uso, DTOs de aplicacion, reglas de autorizacion y transacciones.
- `infrastructure`: persistencia JPA, seguridad JWT, cache, configuraciones y inicializacion de datos.
- `interfaces`: controladores REST, DTOs request/response, manejo global de errores.

El dominio no depende de Spring.

## Ejecucion

```bash
cd backend
mvn spring-boot:run
```

API disponible en `http://localhost:8080`.

Consola H2:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:loansdb`
- User: `sa`
- Password: (No es necesario, no se ingresa nada)

## Credenciales iniciales

- USER
  - email: `usuario@test.com`
  - password: `123`
- ADMIN
  - email: `admin@test.com`
  - password: `123`

## Endpoints principales

### Auth

- `POST /api/auth/login`

Request:

```json
{
  "email": "usuario@test.com",
  "password": "123"
}
```

### Loans

- `POST /api/loans` (USER o ADMIN)
- `GET /api/loans/my` (autenticado)
- `GET /api/loans/{id}` (USER solo su prestamo; ADMIN cualquiera)

Ejemplo create loan:

```json
{
  "amount": 1000,
  "termInMonths": 12
}
```

### Admin

- `GET /api/admin/loans`
- `PATCH /api/admin/loans/{id}/approve`
- `PATCH /api/admin/loans/{id}/reject`

### Users

- `GET /api/users` (ADMIN)
- `GET /api/users/{id}` (ADMIN o el mismo usuario)

## Manejo de errores

Formato estandar:

```json
{
  "timestamp": "2026-05-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El monto del préstamo debe ser mayor que cero",
  "path": "/api/loans"
}
```

Codigos aplicados: `400`, `401`, `403`, `404`, `409`.

## Cache y transacciones

- Cache Caffeine configurada con TTL de 5 minutos.
- `@Cacheable` en consultas de prestamos.
- `@CachePut/@CacheEvict` al aprobar/rechazar/crear.
- `@Transactional` en crear/aprobar/rechazar.
- `@Transactional(readOnly = true)` en consultas.

## Tests

Ejecutar:

```bash
cd backend
mvn test
```

Cobertura incluida:

- Unitarios (`LoanServiceTest`): creacion, validaciones, aprobacion/rechazo, conflicto de estado, autorizacion por usuario.
- Integracion (`AuthAndLoanIntegrationTest`): login exitoso/invalido, crear prestamo con JWT, bloqueo USER en aprobacion, aprobacion ADMIN.
