     document.getElementById('searchButton').addEventListener('click', function() {
            const searchTerm = document.querySelector('.search-input').value;
            if (searchTerm.trim() !== "") {
                window.location.href = `results.html?q=${encodeURIComponent(searchTerm)}`;
            } else {
                alert("Por favor, introduce un término de búsqueda.");
            }
        });