
# Search-engine  - Soogle
---
**Soogle** es un proyecto personal de motor de búsqueda **full-stack** que demuestra la creación de un sistema de búsqueda web desde cero.

## Características Principales

* **Rastreador Web (Web Crawler):** Un `SpiderService` inteligente diseñado para descubrir y descargar contenido web.
    * **Procesamiento Concurrente:** Utiliza **procesamiento paralelo y concurrencia** para optimizar la velocidad y eficiencia del rastreo.
    * **Manejo de Pausas y Errores:** Implementa **retrasos aleatorios** y lógicas de reintento (`429 Too Many Requests`) para evitar ser bloqueado por los servidores.
    * **Extracción de Enlaces Robusta:** Emplea **expresiones regulares (regex)** para extraer URLs limpias y válidas de las páginas.
    * **Extracción de Metadatos:** Capaz de obtener títulos y descripciones (meta tags) de las páginas indexadas.

* **Indexador de Contenido:** Procesa el contenido rastreado y lo almacena eficientemente en una base de datos para futuras búsquedas.
    * **Prevención de Duplicados:** Evita la **reindexación de páginas** y la **inserción de enlaces internos duplicados**.
    * **Indexación Controlada:** Un servicio de `Scheduler` gestiona el rastreo.

* **Backend Robusto (Spring Boot):** Una **API REST** que:
    * Sirve las peticiones de búsqueda desde el frontend.
    * Gestiona la persistencia de los datos indexados (`SearchResultEntity`).

* **Frontend Intuitivo y Reactivo (JavaScript Puro):** Una interfaz de usuario limpia y responsiva.
    * **Interacción Dinámica:** Permite a los usuarios introducir sus consultas de búsqueda y visualizar los resultados.
    * **Comportamiento SPA (Single Page Application):** Optimizado para una experiencia fluida; a partir de la segunda pantalla de búsqueda, la aplicación se comporta como una SPA, actualizando el contenido reactivamente sin recargar el navegador.

---

## 🛠️ Tecnologías Utilizadas

### Backend

* **Java SE** (JDK 17+)
* **Spring Boot** (versión 3.x)
* **Spring REST**
* **Spring Data JPA**
* **Hibernate**
* **Lombok**
* **Base de Datos:** PostgreSQL 

### Frontend

* **JavaScript Puro** (ES6+)
* **Consumo de API:** 
* **Manipulación del DOM**

 Mayo 2025
