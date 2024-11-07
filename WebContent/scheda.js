const API_KEY = "api_key=0da9e6f9c21a8bf6f39971f2b0a732fd";
const BASE_URL = "https://api.themoviedb.org/3";
const IMG_URL = "https://image.tmdb.org/t/p/w500";

// Funzione principale da eseguire al caricamento della pagina
window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const tipo = urlParams.get('tipo'); // Cambiato da 'type' a 'tipo'
    const contentId = urlParams.get('movieId') || urlParams.get('tvId') || urlParams.get('animeId');

    if (contentId && tipo) {
        // Determina l'endpoint API appropriato in base al tipo di contenuto
        let apiUrl;
        if (tipo === "movie") {
            apiUrl = `${BASE_URL}/movie/${contentId}?${API_KEY}`;
        } else if (tipo === "tv") {
            apiUrl = `${BASE_URL}/tv/${contentId}?${API_KEY}`;
        } else if (tipo === "anime") {
            apiUrl = `${BASE_URL}/movie/${contentId}?${API_KEY}`; // TMDB considera gli anime come film
        }

        // Recupera i dettagli del contenuto
        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                console.log(data); // Log per debug
                if (data) {
                    // Popola il titolo
                    document.getElementById('movie-title').innerText = data.title || data.name || "Titolo non disponibile";

                    // Popola la descrizione
                    document.getElementById('movie-overview').innerText = data.overview || "Descrizione non disponibile";

                    // Popola la locandina
                    const posterUrl = data.poster_path ? `${IMG_URL}${data.poster_path}` : "http://via.placeholder.com/200x300";
                    document.getElementById('movie-poster').src = posterUrl;

                    // Recupera e mostra le recensioni associate al contenuto
                    getReviews(contentId, tipo); // Cambiato da 'type' a 'tipo'
                } else {
                    console.error("Errore nel recupero dei dati del contenuto");
                }
            })
            .catch(error => {
                console.error("Errore durante il recupero dei dettagli:", error);
            });
    }

    // Aggiungi logica per salvare la recensione
    const reviewForm = document.getElementById('review-form');
    if (reviewForm) {
        const reviewTextElement = document.getElementById('review-text');
        const submitButton = document.querySelector('button[type="submit"]');

        // Disabilita il pulsante se il campo di testo è vuoto
        reviewTextElement.addEventListener('input', function() {
            submitButton.disabled = !reviewTextElement.value.trim();
        });

        reviewForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const reviewText = reviewTextElement.value;

            if (reviewText) {
                // Crea una richiesta POST per inviare la recensione al server
                fetch(`${contextPath}/aggiungiRecensione`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: `tmdb_id=${contentId}&tipo=${tipo}&commento=${encodeURIComponent(reviewText)}` // Cambiato da 'type' a 'tipo'
                })
                .then(response => {
                    if (response.ok) {
                        // Mostra un messaggio di conferma e ricarica la pagina per vedere la nuova recensione
                        alert("Recensione inviata con successo!");
                        window.location.reload();
                    } else {
                        alert("Errore durante l'invio della recensione. Riprova.");
                    }
                })
                .catch(error => {
                    console.error("Errore durante l'invio della recensione:", error);
                    alert("Errore durante l'invio della recensione.");
                });

                // Svuota il campo di testo della recensione
                reviewTextElement.value = '';
            }
        });
    }
};

function getReviews(contentId, tipo) {
    fetch(`${contextPath}/getRecensioni?tmdb_id=${contentId}&tipo=${tipo}`)  // Assicurati di usare 'tmdb_id' e 'tipo'
        .then(response => response.json())
        .then(data => {
            console.log(data);  // Log per debug
            const userReviews = document.getElementById('user-reviews');
            userReviews.innerHTML = "<h3>Recensioni degli utenti</h3>";  // Ripristina il titolo

            if (data && data.length > 0) {
                data.forEach(review => {
                    const reviewElement = document.createElement('div');
                    reviewElement.classList.add('user-review');

                    // Recupera il nome utente dall'oggetto 'review'
                    const username = review.utente.username || "Utente sconosciuto";  // Assicurati che 'utente' sia parte dell'oggetto review

                    // Visualizza il nome utente e il commento
                    reviewElement.innerHTML = `<p><strong>${username}:</strong> ${review.commento}</p><hr>`;
                    userReviews.appendChild(reviewElement);
                });
            } else {
                userReviews.innerHTML += "<p>Non ci sono ancora recensioni. Sii il primo a scriverne una!</p>";
            }
        })
        .catch(error => console.error("Errore durante il recupero delle recensioni:", error));
}
     
