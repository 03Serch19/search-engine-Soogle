// 1. Cargar el término de búsqueda inicial al cargar la página
const urlParams = new URLSearchParams(window.location.search);
const initialSearchTerm = urlParams.get('q');
const searchInput = document.querySelector('.search-input');
const searchButton = document.querySelector('.search-button');

if (initialSearchTerm) {
    searchInput.value = initialSearchTerm;
    performSearch(initialSearchTerm); // Llama a la función de búsqueda con el término inicial
}

// 2. Manejar el clic del botón de búsqueda
searchButton.addEventListener('click', function() {
    const newSearchTerm = searchInput.value;
    // No redirigimos, simplemente llamamos a la función de búsqueda
    performSearch(newSearchTerm);

    // Opcional: Actualizar la URL sin recargar para que sea compartible
    // Esto es un poco más avanzado pero útil para SPAs
    history.pushState(null, '', `results.html?q=${encodeURIComponent(newSearchTerm)}`);
});

// Opcional: Manejar el Enter en el input de búsqueda
searchInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        searchButton.click(); // Simula el clic en el botón
    }
});

// Función que maneja la lógica de búsqueda y actualización del DOM
async function performSearch(searchTerm) {
    if (searchTerm.trim() === "") {
        alert("Por favor, introduce un término de búsqueda.");
        return; // Detiene la ejecución si el término está vacío
    }

    // Opcional: Mostrar un indicador de carga
    const resultsContainer = document.querySelector('.results-container');
    resultsContainer.innerHTML = '<p>Buscando resultados...</p>'; // Muestra un mensaje mientras carga

    try {
        // Llamada a tu API o backend
        // Y 'searchTerm' se pasaría como un parámetro de consulta o en el cuerpo de la solicitud
        const response = await fetch(`http://localhost:8080/api/v1/results?q=${encodeURIComponent(searchTerm)}`);
     console.log(response); // Para depurar y ver la respuesta de la API
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json(); //converetimos el json devuelto en un objeto js
         console.log(data); // Para depurar y ver la respuesta de la API 
         console.log(data.length); 
        // 2. Limpiar el contenedor de resultados antes de añadir los nuevos
        resultsContainer.innerHTML = '';
     
        console.log(data&& data.length > 0); // Para depurar y ver si hay resultados
        // 3. Iterar sobre los resultados y crear el HTML dinámicamente
        if (data&& data.length > 0) {
            data.forEach(result => {
                const resultItem = document.createElement('div');
                resultItem.classList.add('result-item'); // se añade la clase CSS

                // datos del resultado
                resultItem.innerHTML = `
                    <h2><a href="${result.url}" target="_blank">${result.title}</a></h2>
                    <p>${result.description}</p>
                `;
                resultsContainer.appendChild(resultItem);
            });
        } else {
            resultsContainer.innerHTML = '<p>No se encontraron resultados para tu búsqueda.</p>';
        }

    } catch (error) {
        console.error("Error al realizar la búsqueda:", error);
        resultsContainer.innerHTML = '<p>Lo sentimos, hubo un error al buscar. Inténtalo de nuevo.</p>';
    }
}