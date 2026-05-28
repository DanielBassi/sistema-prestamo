# Frontend - Sistema de Gestion de Prestamos

Aplicacion frontend desarrollada para consumir una API REST de prestamos bancarios en Spring Boot. Incluye autenticacion JWT, manejo de roles USER y ADMIN, y gestion completa de solicitudes de prestamos.

## Stack usado

- React 18+
- Vite
- TypeScript
- React Router DOM
- Axios
- React Hook Form
- Zod
- Zustand
- Tailwind CSS

## Instalacion

```bash
cd frontend
npm install
```

## Ejecutar en desarrollo

```bash
npm run dev
```

La aplicacion se levanta por defecto en `http://localhost:5173`.

## Backend esperado

Configura el backend en:

`http://localhost:8080`

Variables de entorno esperadas:

```env
VITE_API_URL=http://localhost:8080
```

## Credenciales de prueba

- Usuario
  - email: `usuario@test.com`
  - password: `123`
- Administrador
  - email: `admin@test.com`
  - password: `123`

## Rutas principales

- `/login` Inicio de sesion
- `/loans` Panel de usuario USER
- `/admin/loans` Panel administrativo ADMIN
- `/unauthorized` Acceso no autorizado
- `/*` Pagina 404

## Manejo de JWT

- Al iniciar sesion se guarda `token`, `email` y `role` en estado global (Zustand) con persistencia local.
- Axios agrega automaticamente `Authorization: Bearer <token>` en cada request protegida mediante interceptor.
- Si el backend responde `401`, se ejecuta logout automatico y se redirige al login.

## Manejo de roles

- `ProtectedRoute` bloquea rutas privadas cuando no hay sesion.
- `RoleRoute` valida el rol requerido por ruta.
- USER solo accede a `/loans`.
- ADMIN solo accede a `/admin/loans`.

## Build de produccion

```bash
npm run build
```
