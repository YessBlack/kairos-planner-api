# Kairos Planner API

API REST para gestionar tareas y subtareas. El proyecto está construido con Spring Boot y utiliza PostgreSQL para persistir la información.

## API desplegada

La API está disponible en Render:

**Base URL:** <https://kairos-planner-api.onrender.com>

Puedes usar esta URL desde el frontend o para hacer pruebas rápidas. La instancia desplegada debe considerarse un entorno de demo: puede tardar unos segundos en responder después de un periodo de inactividad y los datos dependen de la configuración de la base de datos del despliegue.

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

## Requisitos

- JDK 21 o superior
- Maven 3.9 o superior, o el Maven Wrapper incluido (`./mvnw`)
- PostgreSQL si ejecutas la API localmente

## Ejecución local

1. Clona el repositorio y entra en la carpeta del proyecto.

2. Configura las variables de entorno de la base de datos. Los valores por defecto apuntan a una instalación local de PostgreSQL:

   ```bash
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=kairos-planner
   export DB_USERNAME=postgres
   export DB_PASSWORD=admin
   ```

3. Inicia la aplicación:

   ```bash
   ./mvnw spring-boot:run
   ```

La API local estará disponible en <http://localhost:8080>.

También puedes construir y ejecutar el JAR:

```bash
./mvnw clean package
java -jar target/api-0.0.1-SNAPSHOT.jar
```

## Docker

Construye la imagen:

```bash
docker build -t kairos-planner-api .
```

Ejecuta el contenedor pasando la configuración de PostgreSQL:

```bash
docker run --rm -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=kairos-planner \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=admin \
  kairos-planner-api
```

## Endpoints

La base de las rutas es `/api`.

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/tasks` | Lista todas las tareas |
| `GET` | `/api/tasks/{id}` | Obtiene una tarea por su ID |
| `POST` | `/api/tasks` | Crea una tarea |
| `PUT` | `/api/tasks/{id}` | Actualiza una tarea |
| `DELETE` | `/api/tasks/{id}` | Elimina una tarea |
| `GET` | `/api/tasks/{taskId}/subtasks` | Lista las subtareas de una tarea |
| `POST` | `/api/tasks/{taskId}/subtasks` | Crea una subtarea |
| `PUT` | `/api/tasks/{taskId}/subtasks/{subtaskId}` | Actualiza una subtarea |
| `DELETE` | `/api/tasks/{taskId}/subtasks/{subtaskId}` | Elimina una subtarea |

### Crear una tarea

```bash
curl -X POST https://kairos-planner-api.onrender.com/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Preparar presentación",
    "description": "Revisar el contenido y practicar",
    "deadline": "2026-10-01",
    "status": "NOT_STARTED",
    "subtasks": [
      {
        "text": "Preparar diapositivas",
        "done": false
      }
    ]
  }'
```

`title` y `status` son obligatorios. Los estados disponibles son:

- `NOT_STARTED`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

### Crear una subtarea

```bash
curl -X POST https://kairos-planner-api.onrender.com/api/tasks/1/subtasks \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Revisar ortografía",
    "done": false
  }'
```

El campo `text` es obligatorio en las subtareas.

### Respuestas y errores

- `200 OK`: consulta o actualización exitosa.
- `201 Created`: recurso creado.
- `204 No Content`: recurso eliminado.
- `400 Bad Request`: datos inválidos o campos obligatorios ausentes.
- `404 Not Found`: tarea o subtarea inexistente.

Los errores de validación devuelven un objeto JSON con el campo y el mensaje correspondiente, por ejemplo:

```json
{
  "title": "El título es obligatorio"
}
```

## CORS

La API permite solicitudes desde los clientes configurados en `CorsConfig`, incluidos los entornos locales en el puerto `5500` y los despliegues actuales del frontend.

## Pruebas

Ejecuta las pruebas con:

```bash
./mvnw test
```

## Estructura principal

```text
src/main/java/com/kairosplanner/api
├── config          # Configuración de CORS
├── controller      # Endpoints REST
├── dto             # Objetos de entrada y salida
├── exception       # Excepciones y respuestas de error
├── model           # Entidades y estados de las tareas
├── repository      # Repositorios JPA
└── service         # Lógica de negocio
```

## Configuración de producción

No subas credenciales reales al repositorio. En Render u otro proveedor, configura `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` y `DB_PASSWORD` como variables de entorno del servicio.