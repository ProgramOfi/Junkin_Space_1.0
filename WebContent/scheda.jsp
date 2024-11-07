<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="navbar.css">
    <link rel="stylesheet" href="scheda.css">
    <link rel="stylesheet" href="style.css">
    <title>Junkin Space</title>
</head>

<body>

<script>
    const contextPath = '<%= request.getContextPath() %>';
</script>

<!-- Includi la navbar -->
<div id="navbar"></div>

<script>
    // Carica la navbar
    fetch('navbar.jsp')
    .then(response => response.text())
    .then(data => {
        document.getElementById('navbar').innerHTML = data;
    });
</script>

<main id="main" class="review-page">
    <!-- Sezione superiore: Locandina del film e descrizione -->
    <div class="movie-details-container">
        <img id="movie-poster" class="movie-poster" src="" alt="Movie Poster">
        <div class="movie-description">
            <!-- Intestazione dinamica in base al tipo di contenuto -->
            <h2 id="movie-title">
                <%
                    String tipo = request.getParameter("tipo");
                    String title = request.getParameter("title");
                    String displayTitle = "Titolo non disponibile"; // Valore di default

                    if (tipo != null && title != null) {
                        String contentType = tipo.equals("movie") ? "Film" : tipo.equals("tv") ? "Serie TV" : "Anime";
                        displayTitle = contentType + ": " + title;
                    }
                %>
                <%= displayTitle %>
            </h2>
            <p id="movie-overview"></p>
        </div>
    </div>

    <!-- Sezione delle recensioni -->
    <div class="reviews-section">
        <div class="user-reviews" id="user-reviews">
            <h3>User Reviews</h3>
            <!-- Le recensioni degli utenti verranno aggiunte qui dinamicamente -->
        </div>

        <!-- Sezione per aggiungere una nuova recensione -->
        <div class="add-review">
            <h3>Aggiungi la tua recensione</h3>
            <form id="review-form" action="<%= request.getContextPath() %>/aggiungiRecensione" method="post">
                <textarea id="review-text" name="commento" placeholder="Scrivi qui la tua recensione..." rows="6" cols="50"></textarea>
                
                <!-- Campo nascosto per inviare l'ID (filmId, tvId, animeId) -->
                <input type="hidden" name="tmdb_id" value="<%= request.getParameter("movieId") != null ? request.getParameter("movieId") : request.getParameter("tvId") != null ? request.getParameter("tvId") : request.getParameter("animeId") %>">
                
                <!-- Campo nascosto per inviare il tipo di contenuto -->
                <input type="hidden" name="tipo" value="<%= request.getParameter("tipo") %>">
                
                <button type="submit" class="know-more">Invia Recensione</button>
            </form>
        </div>
    </div>
</main>

<!-- Footer -->
<footer>
    <p>&copy; 2024 Junkin Space</p>
</footer>

<!-- Script per caricamento dinamico -->
<script src="scheda.js"></script>

</body>

</html>
