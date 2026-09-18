# TP1 - Spring Boot, API REST y Arquitectura en Capas

API RESTful desarrolada con con **Spring Boot 3.4.3** para la materia **Web II (UNVIME)**. El proyecto implementa la gestión de productos y favoritos aplicando una arquitectura en capas (`controller`, `service`, `repository`, `dto`, `model`), validaciones de entrada y consumo de servicios externos.

---

## Cómo levantar el proyecto

**Requisitos previos:** Java 21 o superior y Maven.

1. **Clonar el repositorio:**
   ```bash
   git clone ( https://github.com/garavagliaangelina81-creator/TP1-WEB2.git )

   2. **Ejecutar la aplicacion;**
   
   mvn spring-boot:run

   ## Documentacion (swagger UI)

   La Api cuenta con especificación OpenAPI. Con el servidor corriendo, podés explorar y probar todos los endpoints desde la interfaz gráfica accediendo a:

👉 ** ( http://localhost:8080/swagger-ui/index.html )

## Estructura y Arquitectura

El código fuente está organizado bajo el paquete base apiblanck:

apiblanck.config: Configuración de RestClient (integración con la API externa de DummyJSON) y metadatos de Swagger.

apiblanck.controller: Endpoints REST (@RestController) que exponen la interfaz del sistema.

apiblanck.service: Lógica de negocio y orquestación entre repositorios y clientes externos.

apiblanck.repository: Capa de persistencia (implementación @Primary y simulación en memoria).

apiblanck.client: Integración con la API de DummyJSON para verificar productos.

apiblanck.dto & model: Objetos de transferencia de datos y entidades del dominio.

apiblanck.exception: Manejo centralizado y uniforme de errores de la API.


## Endpoints Principales

| Método | Path | Qué hace |
|---|---|---|
| GET | `/api/productos` | Obtiene el listado completo de productos desde el servicio externo |
| GET | `/api/productos/{id}` | Busca y devuelve el detalle de un producto específico por su ID |
| GET | `/api/favoritos` | Devuelve la lista completa de productos guardados como favoritos |
| POST | `/api/favoritos` | Agrega un nuevo producto a la lista de favoritos (valida existencia en la API externa) |
| DELETE | `/api/favoritos/{id}` | Elimina un producto de la lista de favoritos mediante su ID |

### Dependencias Clave

spring-boot-starter-web — Spring MVC y Tomcat embebido.

spring-boot-starter-validation — Validación de datos (@NotNull, @NotBlank, etc.).

springdoc-openapi-starter-webmvc-ui — Generación de interfaz gráfica Swagger UI / OpenAPI.