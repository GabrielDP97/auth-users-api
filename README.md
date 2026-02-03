# Auth Users API 🔐  
**Backend Authentication & Authorization API (Spring Boot)**

Proyecto backend desarrollado en **Java + Spring Boot** enfocado a **autenticación, autorización, seguridad y testing**.

---

## 🎯 Objetivo del proyecto

Construir una API REST que gestione:

- autenticación de usuarios mediante **JWT**
- control de acceso por **roles**
- separación clara de capas (controller, service, repository)
- **tests unitarios reales** con Mockito y JUnit

---

## 🚀 Funcionalidades principales

- Registro de usuarios con contraseña encriptada (BCrypt)
- Login con generación de JWT
- Autenticación basada en token
- Endpoint `/me` para obtener el usuario autenticado
- Endpoint de administración protegido por rol (`ADMIN`)
- Manejo global de errores
- Tests unitarios de servicios y controladores

---

## 🛠️ Stack tecnológico

- **Java**
- **Spring Boot**
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- Hibernate
- MySQL / MariaDB
- Maven
- **JUnit 5**
- **Mockito**

---

## 🧱 Arquitectura

El proyecto sigue una arquitectura clásica en capas:

- **Controller** → gestión de endpoints REST
- **Service** → lógica de negocio
- **Repository** → acceso a datos
- **Security** → JWT, filtros y configuración de seguridad
- **DTOs** → comunicación segura entre cliente y servidor

---

## 🔐 Seguridad

- Contraseñas encriptadas con **BCrypt**
- Autenticación mediante **JWT**
- Control de acceso por roles (`USER`, `ADMIN`)
- Endpoints protegidos a nivel de configuración y anotaciones

---

## 🧪 Testing

El proyecto incluye **tests unitarios reales**, sin levantar el contexto completo de Spring:

### Tests implementados
- `UserServiceTest`
- `AuthControllerTest`
- `AdminControllerTest`

Los tests validan:
- registro de usuarios
- autenticación correcta / incorrecta
- generación de JWT
- comportamiento de controladores
- acceso a endpoints administrativos

Ejecución:
```bash
mvn test
