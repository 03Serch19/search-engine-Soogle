
# ✨ Search-engine  - Soogle
#  Características Principales

* **Rastreador Web (Web Crawler):** Un `SpiderService` inteligente que descubre y descarga contenido web, **utilizando procesamiento paralelo y concurrencia** para optimizar la velocidad y eficiencia del rastreo.
    * **Manejo de Pausas y Errores:** Implementa retrasos aleatorios y lógicas de reintento (`429 Too Many Requests`) para evitar ser bloqueado por los servidores.
    * **Extracción de Enlaces Robusta:** Utiliza expresiones regulares (regex) para extraer URLs limpias de las páginas, ignorando elementos no deseados.
    * **Extracción de Metadatos:** Capaz de obtener títulos y descripciones (meta tags) de las páginas indexadas.
* **Indexador de Contenido:** Procesa el contenido rastreado y lo guarda en una base de datos para futuras búsquedas.
    * **Prevención de Duplicados:** Evita indexar la misma página varias veces o almacenar enlaces internos ya existentes.
    * **Indexación Controlada:** Un servicio de `Scheduler` gestiona el rastreo.
* **Backend Robusto (Spring Boot):** Una API REST que:
    * Sirve las peticiones de búsqueda desde el frontend.
    * Gestiona la persistencia de los datos indexados (`SearchResultEntity`).
* **Frontend Intuitivo (JavaScript Puro):** Una interfaz de usuario limpia y responsiva para que los usuarios puedan introducir sus consultas de búsqueda y visualizar los resultados.
