# BFF Biblioteca — Backend for Frontend

## Descripción General

**BFF Biblioteca** es un Backend for Frontend que actúa como puerta de enlace (gateway) entre aplicaciones cliente y un conjunto de microservicios serverless en Azure Functions.

El BFF expone una API REST unificada y coherente para la gestión de un sistema de biblioteca, centralizando la comunicación con múltiples funciones de Azure que manejan diferentes entidades de negocio (usuarios, libros, autores, préstamos, resúmenes y consultas GraphQL).

### Propósito Principal

- Proporcionar un punto de entrada único para aplicaciones frontend
- Abstraer la complejidad de múltiples microservicios
- Validar y transformar datos antes de enviar a los servicios de negocio
- Manejar errores y proporcionar respuestas consistentes
- Facilitar cambios en la arquitectura de backend sin impactar clientes

---

## Características Principales

✓ **Arquitectura de Gateway**: Centraliza múltiples endpoints de Azure Functions en una API coherente  
✓ **Clientes HTTP Robustos**: Serialización explícita de JSON y manejo robusto de respuestas  
✓ **Configuración Flexible**: Soporta múltiples entornos (desarrollo, staging, producción)  
✓ **Contenedorización Docker**: Imagen multi-arquitectura (amd64 + arm64) en Docker Hub  
✓ **Soporte REST y GraphQL**: Expone rutas para resúmenes y consultas GraphQL del backend  
✓ **Publisher de Event Grid**: Publica eventos de dominio (`Prestamo.Creado`, `Prestamo.Devuelto`, `Usuario.Inactivo`) de forma asíncrona (fire-and-forget)  
✓ **Endpoint de notificaciones**: Expone la lectura de notificaciones generadas por el consumer Event Grid del backend  
✓ **Validación de Datos**: Integración con Jakarta Bean Validation  
✓ **Documentación Completa**: JavaDoc en todas las clases principales  
✓ **Pruebas Automatizadas**: Suite de pruebas unitarias incluida  

---

## ⚡ Colección Postman

El repositorio incluye una colección de Postman lista para ejecutar pruebas CRUD completas:

- Archivo: `postman_collection/sumativa_1_cloud_native_2.postman_collection.json`
- Archivo: `postman_collection/sumativa_2_cloud_native_2.postman_collection.json`
- Cobertura: `usuarios`, `libros`, `autores`, `prestamos`
- Cobertura adicional: `resumen/catalogo`, `resumen/general`, `graphql/catalogo`, `graphql/general`
- Operaciones: `GET`, `GET by id`, `POST`, `PUT`, `DELETE`

La colección permite **tres tipos de pruebas**:

1. **Infraestructura local**: pruebas contra endpoints locales.
2. **Infraestructura en nube pasando por BFF**: validación end-to-end BFF + Azure Functions.
3. **Azure Functions directo (sin BFF)**: validación aislada del backend serverless.

> Esta colección está diseñada para validar flujos CRUD completos.

---

## Requisitos Técnicos

| Componente | Versión | Notas |
|------------|---------|-------|
| **Java** | 21 | JDK requerido para compilación y ejecución |
| **Spring Boot** | 3.3.5 | Framework principal |
| **Maven** | 3.9+ | Gestor de dependencias y construcción |
| **Docker** | 20.10+ | Para contenedorización (opcional pero recomendado) |

---

## Stack Tecnológico

```
├── Java 21
├── Spring Boot 3.3.5
│   ├── spring-boot-starter-web (REST, embedded Tomcat)
│   ├── spring-boot-starter-validation (javax.validation)
│   └── spring-boot-starter-test (JUnit 5, Mockito)
├── Maven 3.9.11
├── Jackson 2.17+ (JSON serialization)
└── Docker (multi-stage build)
```

---

## Estructura del Proyecto

```
bff_libreria/
├── src/
│   ├── main/
│   │   ├── java/cl/duoc/biblioteca/bff/
│   │   │   ├── BffApplication.java          # Punto de entrada Spring Boot
│   │   │   ├── client/
│   │   │   │   ├── FunctionsGatewayClient.java  # Cliente HTTP a Azure Functions
│   │   │   │   ├── EventPublisherClient.java    # Cliente HTTP al publisher de Event Grid
│   │   │   │   └── RestClientConfig.java        # Configuración de RestClient
│   │   │   ├── config/
│   │   │   │   ├── FunctionsProperties.java     # Propiedades inyectadas
│   │   │   │   ├── EventPublisherProperties.java# URL del publisher de Event Grid
│   │   │   │   └── ...
│   │   │   ├── controller/
│   │   │   │   ├── UsuariosController.java      # Endpoints de usuarios (DELETE publica Usuario.Inactivo)
│   │   │   │   ├── LibrosController.java        # Endpoints de libros
│   │   │   │   ├── AutoresController.java       # Endpoints de autores
│   │   │   │   ├── PrestamosController.java     # Endpoints de préstamos (POST/PUT publican eventos)
│   │   │   │   ├── ResumenController.java       # Endpoints de resumen
│   │   │   │   ├── GraphqlController.java       # Endpoints GraphQL
│   │   │   │   └── NotificacionesController.java# Endpoints de notificaciones (passthrough al backend)
│   │   │   └── dto/
│   │   │       ├── UsuarioDto.java
│   │   │       ├── LibroDto.java
│   │   │       ├── AutorDto.java
│   │   │       ├── PrestamoDto.java
│   │   │       └── NotificacionDto.java
│   │   └── resources/
│   │       └── application.yml                   # Configuración global
│   └── test/
│       └── java/cl/duoc/biblioteca/bff/
│           └── BffApplicationTests.java          # Pruebas con TestRestTemplate
├── Dockerfile                                     # Construcción de imagen Docker
├── pom.xml                                        # Configuración Maven
└── README.md                                      # Este archivo
```

---

## Configuración y Variables de Entorno

### application.yml

```yaml
server:
  port: ${SERVER_PORT:8080}           # Puerto de escucha (default: 8080)

spring:
  application:
    name: biblioteca-bff              # Nombre de la aplicación

functions:
  base-url: ${FUNCTIONS_BASE_URL:http://localhost:7071/api}  # URL del backend Azure Functions

event-publisher:
  url: ${EVENT_PUBLISHER_URL:}        # URL completa del publisher de Event Grid (incluye ?code=...)
```

### Variables de Entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `SERVER_PORT` | `8080` | Puerto donde corre el BFF |
| `FUNCTIONS_BASE_URL` | `http://localhost:7071/api` | URL base de Azure Functions (desarrollo local) |
| `EVENT_PUBLISHER_URL` | *(vacío)* | URL completa del publisher de Event Grid del Function App `functioneventrouting`, incluye `?code=<FUNCTION_KEY>`. Si queda vacío, las publicaciones se omiten silenciosamente. |

**Ejemplo para Azure en producción:**
```bash
export SERVER_PORT=8080
export FUNCTIONS_BASE_URL=https://functionsbiblioteca-d4bpb6h8fybvbhac.eastus-01.azurewebsites.net/api
export EVENT_PUBLISHER_URL=https://functioneventrouting-f9bfg5dqcffhftas.eastus-01.azurewebsites.net/api/eventPublisher?code=<REEMPLAZAR_FUNCTION_KEY>
```

> El template completo de variables vive en `.env.example`.

---

## Guía de Inicio Rápido

### 1. Requisitos Previos

- Java 21 instalado (`java -version`)
- Maven 3.9+ instalado (`mvn -version`)
- Git para clonar/acceder al repositorio

### 2. Desarrollo Local (Sin Docker)

#### Compilar el Proyecto

```bash
cd bff_libreria
mvn clean package -DskipTests
```

#### Ejecutar en Desarrollo

**Con Azure Functions locales:**
```bash
# Requisito: Azure Functions debe estar corriendo en localhost:7071
mvn spring-boot:run
```

**Con Azure Functions en remoto:**
```bash
# Apuntar a Azure directamente
export FUNCTIONS_BASE_URL=https://functionsbiblioteca-d4bpb6h8fybvbhac.eastus-01.azurewebsites.net/api
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

#### Ejecutar Pruebas

```bash
mvn test
```

---

## Endpoints Disponibles

Todos los endpoints retornan `application/json` y requieren que Azure Functions esté operativo.

### **Resumen**

#### GET `/api/resumen/catalogo`
Obtiene métricas agregadas del catálogo desde la Function App.

#### GET `/api/resumen/general`
Obtiene métricas agregadas de usuarios y préstamos desde la Function App.

### **GraphQL**

#### POST `/api/graphql/catalogo`
Reenvía consultas GraphQL del dominio catálogo.

#### POST `/api/graphql/general`
Reenvía consultas GraphQL del dominio general.

### **Usuarios**

#### GET `/api/usuarios`
Obtiene la lista completa de usuarios.

**Respuesta (200 OK):**
```json
[
  {
    "id": "1",
    "nombre": "Juan",
    "apellidoPaterno": "González",
    "apellidoMaterno": "López",
    "email": "juan.gonzalez@correo.cl",
    "activo": true
  },
  {
    "id": "2",
    "nombre": "María",
    "apellidoPaterno": "Rodríguez",
    "apellidoMaterno": "Martínez",
    "email": "maria.rodriguez@correo.cl", 
    "activo": true
  }
]
```

#### GET `/api/usuarios/{id}`
Obtiene un usuario específico por ID.

**Parámetros:**
- `id` (path): Identificador del usuario (string numérico)

**Respuesta (200 OK):**
```json
{
  "id": "1",
  "nombre": "Juan",
  "apellidoPaterno": "González",
  "apellidoMaterno": "López",
  "email": "juan.gonzalez@correo.cl",
  "activo": true
}
```

**Error (404 Not Found):**
```json
{
  "error": "Usuario no encontrado",
  "id": "999"
}
```

#### POST `/api/usuarios`
Crea un nuevo usuario.

**Body (Content-Type: application/json):**
```json
{
  "nombre": "Carlos",
  "apellidoPaterno": "Pérez",
  "apellidoMaterno": "García",
  "email": "carlos.perez@correo.cl",
  "activo": true
}
```

**Respuesta (201 Created):**
```json
{
  "id": "3",
  "nombre": "Carlos",
  "apellidoPaterno": "Pérez",
  "apellidoMaterno": "García",
  "email": "carlos.perez@correo.cl",
  "activo": true
}
```

#### PUT `/api/usuarios/{id}`
Actualiza un usuario existente.

**Parámetros:**
- `id` (path): Identificador del usuario

**Body:**
```json
{
  "nombre": "Carlos Actualizado",
  "apellidoPaterno": "Pérez",
  "apellidoMaterno": "García",
  "email": "carlos.nuevo@correo.cl",
  "activo": true
}
```

**Respuesta (200 OK):** El usuario actualizado

#### DELETE `/api/usuarios/{id}`
Elimina un usuario (o lo marca inactivo si tiene préstamos pendientes).

**Respuesta (200 OK):**
```json
{
  "mensaje": "Usuario eliminado exitosamente",
  "id": "3"
}
```

> **Evento publicado:** si el usuario queda **inactivo** por tener préstamos activos, el BFF publica de forma asíncrona el evento `Usuario.Inactivo` al Event Grid Topic. La respuesta HTTP no espera al broker (fire-and-forget).

---

### **Libros**

#### GET `/api/libros`
Obtiene la lista completa de libros.

**Respuesta:**
```json
[
  {
    "id": "1",
    "titulo": "El Quijote",
    "autor": "Miguel de Cervantes",
    "isbn": "978-0-14-043959-4",
    "disponible": true
  }
]
```

#### GET `/api/libros/{id}`
Obtiene un libro específico.

#### POST `/api/libros`
Crea un nuevo libro.

**Body:**
```json
{
  "titulo": "Cien Años de Soledad",
  "autor": "Gabriel García Márquez",
  "isbn": "978-0-06-085435-8",
  "disponible": true
}
```

#### PUT `/api/libros/{id}`
Actualiza un libro existente.

#### DELETE `/api/libros/{id}`
Elimina un libro.

---

### **Autores**

#### GET `/api/autores`
Obtiene la lista de autores.

#### GET `/api/autores/{id}`
Obtiene un autor específico.

#### POST `/api/autores`
Crea un nuevo autor.

**Body:**
```json
{
  "nombre": "Jorge Luis",
  "apellidoPaterno": "Borges",
  "apellidoMaterno": "Acevedo",
  "nacionalidad": "Argentino"
}
```

#### PUT `/api/autores/{id}`
Actualiza un autor.

#### DELETE `/api/autores/{id}`
Elimina un autor.

---

### **Préstamos**

#### GET `/api/prestamos`
Obtiene la lista de préstamos.

**Respuesta:**
```json
[
  {
    "id": "1",
    "usuarioId": "1",
    "libroId": "1",
    "fechaPrestamo": "2026-03-25",
    "fechaDevolucion": "2026-04-08",
    "estado": "activo"
  }
]
```

#### GET `/api/prestamos/{id}`
Obtiene un préstamo específico.

#### POST `/api/prestamos`
Crea un nuevo préstamo.

**Body:**
```json
{
  "usuarioId": "1",
  "libroId": "1",
  "fechaPrestamo": "2026-03-31",
  "fechaDevolucion": "2026-04-14"
}
```

> **Evento publicado:** tras la creación, el BFF publica `Prestamo.Creado` al Event Grid Topic (fire-and-forget).

#### PUT `/api/prestamos/{id}`
Actualiza un préstamo existente.

> **Evento publicado:** si el `estado` queda en `DEVUELTO`, el BFF publica `Prestamo.Devuelto` al Event Grid Topic (fire-and-forget). Otros cambios de estado no emiten evento.

#### DELETE `/api/prestamos/{id}`
Elimina un préstamo.

---

## Flujo event-driven (Publisher)

El BFF actúa como **publisher** dentro de la arquitectura event-driven. Ciertas operaciones REST disparan, además de la respuesta sincrónica, una publicación asíncrona al Event Grid Topic vía el Function App `functioneventrouting`.

| Operación REST                          | Evento publicado     | `subject`                               |
|-----------------------------------------|----------------------|-----------------------------------------|
| `POST /api/prestamos`                   | `Prestamo.Creado`    | `biblioteca/prestamos/{id}`             |
| `PUT /api/prestamos/{id}` con `estado=DEVUELTO` | `Prestamo.Devuelto` | `biblioteca/prestamos/{id}`             |
| `DELETE /api/usuarios/{id}` (resulta en inactivo) | `Usuario.Inactivo` | `biblioteca/usuarios/{id}`              |

```
[Cliente] → BFF (POST/PUT/DELETE)
              ├─→ Azure Functions CRUD (sincrónico, response al cliente)
              └─→ EventPublisherClient.publishAsync() (fire-and-forget)
                        ↓
                  eventPublisher (functioneventrouting)
                        ↓ sendEvent()
                  Event Grid Topic: biblioteca-topics
                        ↓ Event Subscription
                  notificacionConsumer (functionsbiblioteca)
                        ↓ persiste
                  Tabla NOTIFICACIONES
                        ↑ visible en
                  GET /api/notificaciones (passthrough del BFF)
```

**Características:**
- **Asíncrono:** la publicación corre en `CompletableFuture.runAsync`, la respuesta HTTP no espera al broker.
- **Tolerante a fallo de publicación:** si el publisher está caído o `EVENT_PUBLISHER_URL` no está configurado, la operación CRUD igual responde 200/201 al cliente; la publicación se loggea como warning.
- **Desacoplado:** el BFF no conoce al consumer ni al Topic — solo conoce la URL del publisher.

---

### **Notificaciones**

Endpoints de **lectura** que actúan como passthrough hacia la función `notificaciones` del backend (`functionsbiblioteca`). Las notificaciones son materializadas por el consumer Event Grid (`notificacionConsumer`) en respuesta a los eventos publicados por este mismo BFF.

#### GET `/api/notificaciones`
Lista todas las notificaciones generadas por el consumer (más recientes primero).

**Respuesta (200 OK):**
```json
[
  {
    "id": "12",
    "idUsuario": "1",
    "tipo": "PRESTAMO_CREADO",
    "asunto": "Préstamo registrado",
    "cuerpo": "Tu préstamo del libro 5 ha sido registrado.",
    "estado": "PENDIENTE",
    "fechaCreacion": "2026-05-03T10:30:00Z",
    "fechaEnvio": null
  }
]
```

#### GET `/api/notificaciones/{idUsuario}`
Lista notificaciones de un usuario específico.

**Parámetros:**
- `idUsuario` (path): identificador del usuario

> **Nota:** estos endpoints son de **solo lectura**. La generación de notificaciones es asíncrona y sucede cuando el consumer del backend recibe un evento del Topic — esperar 5–10 s tras una operación que publique evento antes de consultar.

---

## Casos de Prueba

### Pruebas con Colección Postman

Colección disponible en:

- `postman_collection/sumativa_1_cloud_native_2.postman_collection.json`
- `postman_collection/sumativa_2_cloud_native_2.postman_collection.json`

Modos de ejecución incluidos en la colección:

1. **CRUD completo en entorno local**
  - Objetivo: validar comportamiento local de servicios y rutas.
2. **CRUD completo en nube pasando por BFF**
  - Objetivo: validar integración end-to-end con el gateway.
3. **CRUD completo directo a Azure Functions (sin BFF)**
  - Objetivo: validar backend serverless de forma aislada.

4. **Resumen y GraphQL desde BFF**
  - Objetivo: validar los nuevos endpoints de resumen y GraphQL expuestos por el gateway.

Recomendación operativa:

- Ejecutar los tres modos para cubrir regresiones de enrutamiento, serialización y contrato HTTP.

### Escenario 1: Crear un Usuario y Consultar

```bash
# 1. Crear usuario
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana",
    "apellidoPaterno": "López",
    "apellidoMaterno": "Rodríguez",
    "email": "ana.lopez@correo.cl",
    "activo": true
  }'

# Respuesta esperada:
# HTTP 201 (o 200) con el usuario creado incluyendo el ID

# 2. Consultar usuario creado (reemplazar {ID} con el valor obtenido)
curl -X GET http://localhost:8080/api/usuarios/{ID}
```

### Escenario 2: Crear Libro y Consultar Listado

```bash
# 1. Crear libro
curl -X POST http://localhost:8080/api/libros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "La Casa de los Espíritus",
    "autor": "Isabel Allende",
    "isbn": "978-0-553-38251-1",
    "disponible": true
  }'

# 2. Obtener todos los libros
curl -X GET http://localhost:8080/api/libros
```

### Escenario 3: Actualizar Usuario

```bash
# Actualizar usuario con ID=1
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana María",
    "apellidoPaterno": "López",
    "apellidoMaterno": "García",
    "email": "anam.lopez@correo.cl",
    "activo": true
  }'
```

### Escenario 4: Crear Préstamo y Consultar

```bash
# 1. Crear préstamo (con usuario y libro existentes)
curl -X POST http://localhost:8080/api/prestamos \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": "1",
    "libroId": "1",
    "fechaPrestamo": "2026-03-31",
    "fechaDevolucion": "2026-04-14"
  }'

# 2. Consultar préstamo
curl -X GET http://localhost:8080/api/prestamos/1
```

### Escenario 5: Borrar Entidad

```bash
# Eliminar usuario
curl -X DELETE http://localhost:8080/api/usuarios/3

# Nota: Si el usuario tiene préstamos activos, 
# será marcado como inactivo en lugar de eliminado
```

---

## Docker

### Imagen Disponible en Docker Hub

La imagen del BFF está publicada en Docker Hub bajo el repositorio **`dimmox/biblioteca-bff`**.

Plataformas soportadas:
- `linux/amd64` (servidores, desarrollo en Linux/Windows)
- `linux/arm64` (MacBook con chip M1/M2/M3, etc.)

### Ejecutar Contenedor Docker

#### 1. Descarga la Imagen (Opcional)

```bash
# Descargar la última versión
docker pull dimmox/biblioteca-bff:latest

# O una versión específica (si está disponible)
docker pull dimmox/biblioteca-bff:1.0.0
```

#### 2. Ejecutar el Contenedor

**Modo Desarrollo (Azure Functions local):**
```bash
docker run -d \
  --name bff-biblioteca \
  -p 8080:8080 \
  -e FUNCTIONS_BASE_URL=http://host.docker.internal:7071/api \
  dimmox/biblioteca-bff:latest
```

**Modo Producción (Azure Functions en la nube):**
```bash
docker run -d \
  --name bff-biblioteca \
  -p 8080:8080 \
  -e SERVER_PORT=8080 \
  -e FUNCTIONS_BASE_URL=https://functionsbiblioteca-d4bpb6h8fybvbhac.eastus-01.azurewebsites.net/api \
  -e EVENT_PUBLISHER_URL='https://functioneventrouting-f9bfg5dqcffhftas.eastus-01.azurewebsites.net/api/eventPublisher?code=<REEMPLAZAR_FUNCTION_KEY>' \
  dimmox/biblioteca-bff:latest
```

> Si vas a parametrizar varias variables, prefiere `--env-file .env` apuntando a un archivo `.env` local (basado en `.env.example`).

#### 3. Verificación del Contenedor en Ejecución

```bash
# Ver contenedores activos
docker ps

# Ver logs del contenedor
docker logs bff-biblioteca

# Probar endpoint
curl http://localhost:8080/api/usuarios
```

#### 4. Detener el Contenedor

```bash
docker stop bff-biblioteca
docker rm bff-biblioteca
```

### Construcción Manual de Imagen (para cambios personalizados)

```bash
# Compilar y construir imagen
docker build -t mi-bff-biblioteca:1.0.0 .

# Ejecutar con la imagen construida
docker run -d \
  --name bff-mi-local \
  -p 8080:8080 \
  -e FUNCTIONS_BASE_URL=http://host.docker.internal:7071/api \
  mi-bff-biblioteca:1.0.0
```

### Docker Compose (Facilita levantamiento local)

Crea archivo `docker-compose.yml` en el raíz del proyecto:

```yaml
version: '3.8'

services:
  bff:
    image: dimmox/biblioteca-bff:latest
    container_name: bff-biblioteca
    ports:
      - "8080:8080"
    environment:
      - SERVER_PORT=8080
      - FUNCTIONS_BASE_URL=http://host.docker.internal:7071/api
      - EVENT_PUBLISHER_URL=${EVENT_PUBLISHER_URL:-}   # opcional: URL completa del publisher de Event Grid
    # En caso de usar redes de contenedores personalizadas, ajustar host.docker.internal en consecuencia
```

Ejecutar:
```bash
docker-compose up -d
```

---

## Despliegue

### Despliegue en Servidor (Máquinas Linux)

1. **Conectar por SSH a servidor**
```bash
ssh usuario@ip
```

2. **Reproducir contenedor**
```bash
docker run -d \
  --name bff-biblioteca-prod \
  -p 8080:8080 \
  --env-file .env \
  dimmox/biblioteca-bff:latest
```

> El `.env` debe contener `SERVER_PORT`, `FUNCTIONS_BASE_URL` y `EVENT_PUBLISHER_URL` (ver `.env.example`).

3. **Verificación de accesibilidad**
```bash
curl http://localhost:8080/api/usuarios
```

### Monitoreo Básico

```bash
# Ver logs en tiempo real
docker logs -f bff-biblioteca-prod

# Ver consumo de recursos
docker stats bff-biblioteca-prod

# Reiniciar si es necesario
docker restart bff-biblioteca-prod
```

---

## Estructura del Código y Patrones

### Componentes Principales

#### 1. **BffApplication.java**
Punto de entrada Spring Boot. Contiene el método `main()` que inicia la aplicación.

#### 2. **FunctionsGatewayClient.java**
Cliente HTTP que se comunica con Azure Functions. Responsable de:
- Construir peticiones HTTP (GET, POST, PUT, DELETE)
- Serializar/deserializar JSON
- Manejo de errores de conectividad
- Conversión de respuestas a DTOs

Métodos principales:
- `getUsuarios()`, `getUsuarioById()`, `createUsuario()`, `updateUsuario()`, `deleteUsuario()`
- `getLibros()`, `getLibroById()`, `createLibro()`, `updateLibro()`, `deleteLibro()`
- `getAutores()`, `getAutorById()`, `createAutor()`, `updateAutor()`, `deleteAutor()`
- `getPrestamos()`, `getPrestamoById()`, `createPrestamo()`, `updatePrestamo()`, `deletePrestamo()`
- `getNotificaciones()`, `getNotificacionesByUsuario()`

#### 3. **Controllers**
Exponen los endpoints REST. Configurados con `@RestController` y manejan:
- Validación de parámetros
- Llamadas a `FunctionsGatewayClient`
- Conversión de respuestas a JSON

#### 4. **DTOs (Data Transfer Objects)**
Clases que representan estructuras de datos:
- `UsuarioDto`
- `LibroDto`
- `AutorDto`
- `PrestamoDto`
- `NotificacionDto`

#### 5. **FunctionsProperties.java**
Clase de configuración que inyecta variables de entorno mediante `@ConfigurationProperties`.

#### 6. **EventPublisherProperties.java**
Clase de configuración (`@ConfigurationProperties(prefix = "event-publisher")`) que resuelve la URL del publisher de Event Grid desde `application.yml` / variable `EVENT_PUBLISHER_URL`.

#### 7. **EventPublisherClient.java**
Cliente HTTP fire-and-forget hacia el Function App `functioneventrouting`. Empaqueta cada evento como `{eventType, subject, data}` y lo publica de forma asíncrona vía `CompletableFuture.runAsync`, sin bloquear la respuesta REST del BFF.

### Patrones Implementados

**Gateway Pattern**: El BFF actúa como punto único de entrada a múltiples servicios.

**DTO Pattern**: Separación clara entre modelo de transfer y lógica de negocio.

**Dependency Injection**: Spring inyecta todas las dependencias automáticamente.

**RestClient**: Cliente HTTP moderno (reemplazo de RestTemplate en Spring Boot 3.2+).

**Publisher / Event-Driven**: Operaciones REST seleccionadas publican eventos de dominio al Event Grid Topic vía `EventPublisherClient`, en modo fire-and-forget para no acoplar la latencia del cliente al broker.

---

## Troubleshooting

### Error: "Unable to connect to Functions backend"

**Causa:** FUNCTIONS_BASE_URL no es accesible o Azure Functions no está corriendo.

**Solución:**
```bash
# Verificar que Azure Functions está operativo
curl https://functionsbiblioteca-d4bpb6h8fybvbhac.eastus-01.azurewebsites.net/api/usuarios

# Para entornos de desarrollo local, verificar:
# 1. Azure Functions Core Tools está en ejecución
# 2. Escucha en puerto 7071
# 3. FUNCTIONS_BASE_URL está configurado correctamente
```

### Error: "Body requerido" (400 Bad Request) en POST/PUT

**Causa:** Serialización incorrecta del JSON.

**Solución:** El BFF ya incluye serialización explícita. Debe incluirse el header `Content-Type: application/json`.

```bash
# Correcto
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test",...}'

# Incorrecto (sin header)
curl -X POST http://localhost:8080/api/usuarios \
  -d '{"nombre":"Test",...}'
```

### Error: "Port 8080 already in use"

**Causa:** Otro proceso ya usa puerto 8080.

**Solución:**
```bash
# Opción 1: Usar puerto diferente
export SERVER_PORT=9090
mvn spring-boot:run

# Opción 2: Matar proceso que usa puerto 8080
lsof -i :8080
kill -9 <PID>
```

### Error: "Compile failure"

**Causa:** Dependencia de Maven no descargada correctamente.

**Solución:**
```bash
# Limpiar caché de Maven y reintentar
mvn clean package -U
```
