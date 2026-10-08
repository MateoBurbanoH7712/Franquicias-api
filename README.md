# API de Gestión de Franquicias

API REST desarrollada con **Java 17 y Spring Boot 3.5.6** para administrar franquicias, sus sucursales y los productos de cada sucursal. Permite gestionar inventarios, consultar el producto con mayor stock por sucursal y actualizar los nombres de los recursos.

El proyecto incluye persistencia en **PostgreSQL**, documentación interactiva con **Swagger/OpenAPI** y ejecución conjunta de aplicación y base de datos mediante **Docker Compose**.

## Tecnologías utilizadas

| Tecnología | Uso |
| --- | --- |
| Java 17 | Lenguaje de programación |
| Spring Boot 3.5.6 | Desarrollo de la API REST |
| Spring Web | Controladores y endpoints HTTP |
| Spring Data JPA / Hibernate | Persistencia y acceso a datos |
| PostgreSQL 16 | Base de datos relacional |
| Jakarta Bean Validation | Validación de solicitudes |
| Maven / Maven Wrapper | Compilación y dependencias |
| springdoc-openapi / Swagger UI | Documentación interactiva |
| Docker y Docker Compose | Contenedorización y ejecución local |

## Modelo de datos

- **Franquicia:** tiene un identificador y un nombre; puede contener varias sucursales.
- **Sucursal:** tiene un identificador y un nombre; pertenece a una franquicia y puede contener varios productos.
- **Producto:** tiene un identificador, un nombre y una cantidad de stock; pertenece a una sucursal.

Relaciones: `Franquicia (1) → (N) Sucursal (1) → (N) Producto`.

## Requisitos para ejecutar con Docker

- **Docker Desktop** instalado y en ejecución (incluye Docker Compose), o Docker Engine con el complemento Compose.
- **Git**, para clonar el repositorio (también puedes descargar el ZIP desde GitHub).
- Puertos **8080** y **5432** disponibles en tu equipo.

> **No es necesario instalar Java, Maven ni PostgreSQL localmente** si ejecutas el proyecto mediante Docker.

## Instalación y ejecución con Docker

### 1. Descargar el proyecto

```bash
git clone URL_DEL_REPOSITORIO
cd api-franquicias
```

Sustituye `URL_DEL_REPOSITORIO` por la URL HTTPS real de este repositorio. Si descargas el ZIP, descomprímelo y abre una terminal en la carpeta donde está `docker-compose.yml`.

### 2. Construir e iniciar los servicios

```bash
docker compose up -d --build
```

Este comando construye la imagen de la API a partir del `Dockerfile`, inicia PostgreSQL, espera a que la base de datos esté saludable e inicia Spring Boot en segundo plano. La primera ejecución puede tardar varios minutos.

### 3. Verificar el estado

```bash
docker compose ps
```

Deberías ver los servicios `api` y `postgres` en ejecución; PostgreSQL debería indicar `healthy`.

Para consultar los logs:

```bash
docker compose logs -f api
```

Cuando aparezca `Started ApiFranquiciasApplication`, la API estará lista.

### 4. Abrir la API

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Listar franquicias:** http://localhost:8080/franquicias

La base de datos se ejecuta en el servicio `postgres` de Docker Compose. La API utiliza `jdbc:postgresql://postgres:5432/franquicias_db` dentro de la red de contenedores.

### 5. Detener o reiniciar

```bash
# Detener y retirar los contenedores sin eliminar el volumen de datos
docker compose down

# Volver a iniciarlos
docker compose up -d

# Reconstruir después de modificar código o recursos de la aplicación
docker compose up -d --build
```

**Importante:** evita `docker compose down -v` si deseas conservar los datos: la opción `-v` elimina los volúmenes administrados por Compose, incluido el de PostgreSQL.

> **Configuración de ejemplo para desarrollo local:** el archivo `docker-compose.yml` utiliza credenciales de PostgreSQL destinadas únicamente a pruebas locales. Para un entorno público o de producción, usa secretos o variables de entorno seguras, cambia las contraseñas y evita exponer innecesariamente el puerto de la base de datos.

## Referencia de endpoints

URL base local: `http://localhost:8080`

Todos los endpoints que reciben un cuerpo JSON deben enviarse con `Content-Type: application/json`. Los identificadores (`id`, `franquiciaId`, `sucursalId`) son numéricos y dependen de los registros existentes. Los ejemplos siguientes usan IDs ilustrativos; debes sustituirlos por los que devuelva tu propia API.

### Franquicias

#### 1. Crear franquicia

`POST /franquicias` — **201 Created**

**Request**
```json
{
  "nombre": "Reto Empanadas"
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Reto Empanadas"
}
```

#### 2. Listar franquicias

`GET /franquicias` — **200 OK**

**Response**
```json
[
  { "id": 1, "nombre": "Reto Empanadas" },
  { "id": 2, "nombre": "Franquicia Centro" }
]
```

#### 3. Consultar franquicia por ID

`GET /franquicias/1` — **200 OK**

**Response**
```json
{
  "id": 1,
  "nombre": "Reto Empanadas"
}
```

#### 4. Consultar productos con mayor stock por sucursal

`GET /franquicias/1/productos-mayor-stock` — **200 OK**

Devuelve **un producto con el mayor stock por cada sucursal** de la franquicia. Las sucursales sin productos no aparecen en la lista. Si hay empate en stock, se selecciona el producto con menor ID.

**Response**
```json
[
  {
    "sucursalId": 1,
    "sucursalNombre": "Sucursal Norte",
    "productoId": 2,
    "productoNombre": "Empanada de pollo",
    "stock": 250
  },
  {
    "sucursalId": 2,
    "sucursalNombre": "Sucursal Sur",
    "productoId": 4,
    "productoNombre": "Empanada de queso",
    "stock": 300
  }
]
```

Si la franquicia existe pero no tiene productos, la respuesta es `[]`.

#### 5. Renombrar franquicia

`PATCH /franquicias/1/nombre` — **200 OK**

**Request**
```json
{
  "nombre": "Reto Empanadas Premium"
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Reto Empanadas Premium"
}
```

### Sucursales

#### 6. Crear sucursal en una franquicia

`POST /franquicias/1/sucursales` — **201 Created**

**Request**
```json
{
  "nombre": "Sucursal Norte"
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Sucursal Norte",
  "franquiciaId": 1
}
```

#### 7. Listar sucursales de una franquicia

`GET /franquicias/1/sucursales` — **200 OK**

**Response**
```json
[
  {
    "id": 1,
    "nombre": "Sucursal Norte",
    "franquiciaId": 1
  },
  {
    "id": 2,
    "nombre": "Sucursal Sur",
    "franquiciaId": 1
  }
]
```

#### 8. Consultar sucursal por ID

`GET /sucursales/1` — **200 OK**

**Response**
```json
{
  "id": 1,
  "nombre": "Sucursal Norte",
  "franquiciaId": 1
}
```

#### 9. Renombrar sucursal

`PATCH /sucursales/1/nombre` — **200 OK**

**Request**
```json
{
  "nombre": "Sucursal Norte Renovada"
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Sucursal Norte Renovada",
  "franquiciaId": 1
}
```

### Productos

#### 10. Crear producto en una sucursal

`POST /sucursales/1/productos` — **201 Created**

**Request**
```json
{
  "nombre": "Empanada de pollo",
  "stock": 150
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Empanada de pollo",
  "stock": 150,
  "sucursalId": 1
}
```

#### 11. Listar productos de una sucursal

`GET /sucursales/1/productos` — **200 OK**

**Response**
```json
[
  {
    "id": 1,
    "nombre": "Empanada de pollo",
    "stock": 150,
    "sucursalId": 1
  },
  {
    "id": 2,
    "nombre": "Empanada de carne",
    "stock": 100,
    "sucursalId": 1
  }
]
```

#### 12. Consultar producto por ID

`GET /productos/1` — **200 OK**

**Response**
```json
{
  "id": 1,
  "nombre": "Empanada de pollo",
  "stock": 150,
  "sucursalId": 1
}
```

#### 13. Actualizar stock de un producto

`PUT /productos/1/stock` — **200 OK**

**Request**
```json
{
  "stock": 250
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Empanada de pollo",
  "stock": 250,
  "sucursalId": 1
}
```

#### 14. Eliminar producto

`DELETE /productos/1` — **204 No Content**

**Response:** sin cuerpo. El producto deja de estar disponible en las consultas posteriores.

#### 15. Renombrar producto

`PATCH /productos/1/nombre` — **200 OK**

**Request**
```json
{
  "nombre": "Empanada de pollo especial"
}
```

**Response**
```json
{
  "id": 1,
  "nombre": "Empanada de pollo especial",
  "stock": 250,
  "sucursalId": 1
}
```

## Validaciones y respuestas HTTP

| Código | Significado |
| --- | --- |
| `200 OK` | Consulta o actualización exitosa |
| `201 Created` | Recurso creado correctamente |
| `204 No Content` | Producto eliminado correctamente |
| `400 Bad Request` | Datos de entrada inválidos |
| `404 Not Found` | No existe el recurso solicitado |

Reglas principales de validación:

- El nombre de franquicias, sucursales y productos no puede estar vacío ni contener solo espacios.
- El stock es obligatorio al crear un producto o actualizar su inventario.
- El stock debe ser un número entero **mayor o igual a cero**.
- Para crear una sucursal debe existir la franquicia; para crear un producto debe existir la sucursal.

El formato concreto de las respuestas de error depende de la configuración de manejo de excepciones de Spring Boot.

## Flujo de prueba recomendado

1. Crear una franquicia con `POST /franquicias` y guardar el ID recibido.
2. Crear dos sucursales con `POST /franquicias/{franquiciaId}/sucursales`.
3. Crear varios productos en cada sucursal con `POST /sucursales/{sucursalId}/productos`.
4. Actualizar el stock de uno de ellos con `PUT /productos/{id}/stock`.
5. Consultar `GET /franquicias/{id}/productos-mayor-stock` y verificar el resultado.
6. Probar los tres endpoints `PATCH` de cambio de nombre.
7. Eliminar un producto con `DELETE /productos/{id}`.

Puedes realizar todas estas pruebas desde Swagger UI, sin instalar Postman.

## Estructura general del proyecto

```text
api-franquicias/
├── src/
│   └── main/
│       ├── java/com/example/api_franquicias/
│       │   ├── config/          # Configuración de OpenAPI
│       │   ├── controller/      # Endpoints REST
│       │   ├── dto/
│       │   │   ├── request/     # Datos de entrada
│       │   │   └── response/    # Datos de salida
│       │   ├── entity/          # Entidades JPA
│       │   ├── repository/      # Acceso a PostgreSQL
│       │   └── service/         # Lógica de negocio
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── pom.xml
└── README.md
```

## Solución de problemas

**El puerto 8080 está ocupado:** detén la aplicación Spring Boot que se esté ejecutando fuera de Docker, o libera el puerto antes de iniciar Compose.

**El puerto 5432 está ocupado:** verifica que no exista otra instancia local de PostgreSQL usando ese puerto.

**La API no inicia:** consulta `docker compose logs --tail=100 api` y verifica que PostgreSQL aparezca como `healthy` con `docker compose ps`.

**Modifiqué el código pero Docker sigue mostrando la versión anterior:** ejecuta `docker compose up -d --build` para reconstruir la imagen.

**No veo datos al descargar el proyecto:** es normal; cada instalación tiene su propia base de datos y volumen. Debes crear los registros utilizando los endpoints. El volumen conserva los datos entre reinicios normales de los contenedores.

## Alcance del proyecto

Este repositorio está preparado para ejecutarse **localmente con Docker Compose**. El despliegue público en plataformas como Render no forma parte de las instrucciones actuales.
