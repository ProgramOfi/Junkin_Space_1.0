const API_KEY = "api_key=0da9e6f9c21a8bf6f39971f2b0a732fd";
const BASE_URL = "https://api.themoviedb.org/3";
const API_URL = BASE_URL + "/discover/tv?sort_by=popularity.desc&" + API_KEY;
const IMG_URL = "https://image.tmdb.org/t/p/w500";
const searchURL = BASE_URL + "/search/tv?" + API_KEY;

window.addEventListener('scroll', function() {
    var header = document.querySelector('header');
    header.classList.toggle('sticky', window.scrollY > 0);
});

const genres = [
    { id: 28, name: "Action" },
    { id: 12, name: "Adventure" },
    { id: 16, name: "Animation" },
    { id: 35, name: "Comedy" },
    { id: 80, name: "Crime" },
    { id: 99, name: "Documentary" },
    { id: 18, name: "Drama" },
    { id: 10751, name: "Family" },
    { id: 14, name: "Fantasy" },
    { id: 36, name: "History" },
    { id: 27, name: "Horror" },
    { id: 10402, name: "Music" },
    { id: 9648, name: "Mystery" },
    { id: 10749, name: "Romance" },
    { id: 878, name: "Science Fiction" },
    { id: 10770, name: "TV Movie" },
    { id: 53, name: "Thriller" },
    { id: 10752, name: "War" },
    { id: 37, name: "Western" },
];

// Elementi DOM
const main = document.getElementById("main");
const form = document.getElementById("form");
const search = document.getElementById("search");
const tagsEl = document.getElementById("tags");

// Creazione dinamica dei tag per i generi
var selectedGenre = [];
setGenre();
function setGenre() {
    tagsEl.innerHTML = "";
    genres.forEach((genre) => {
        const t = document.createElement("div");
        t.classList.add("tag");
        t.id = genre.id;
        t.innerText = genre.name;
        t.addEventListener("click", () => {
            if (selectedGenre.length === 0) {
                selectedGenre.push(genre.id);
            } else {
                if (selectedGenre.includes(genre.id)) {
                    selectedGenre.forEach((id, idx) => {
                        if (id == genre.id) {
                            selectedGenre.splice(idx, 1);
                        }
                    });
                } else {
                    selectedGenre.push(genre.id);
                }
            }
            getTVShows(API_URL + "&with_genres=" + encodeURI(selectedGenre.join(",")));
            highlightSelection();
        });
        tagsEl.append(t);
    });
}

// Evidenzia i generi selezionati
function highlightSelection() {
    const tags = document.querySelectorAll(".tag");
    tags.forEach((tag) => {
        tag.classList.remove("highlight");
    });
    if (selectedGenre.length !== 0) {
        selectedGenre.forEach((id) => {
            const highlightedTag = document.getElementById(id);
            highlightedTag.classList.add("highlight");
        });
    }
}

// Recupera le serie TV dall'API
getTVShows(API_URL);

function getTVShows(url) {
    fetch(url)
        .then((res) => res.json())
        .then((data) => {
            if (data.results.length !== 0) {
                showTVShows(data.results);
            } else {
                main.innerHTML = `<h1 class="no-results">Nessun risultato trovato</h1>`;
            }
        });
}

// Mostra le serie TV recuperate nella pagina
function showTVShows(data) {
    main.innerHTML = "";

    data.forEach((tvShow) => {
        const { name, poster_path, vote_average, overview, id } = tvShow;
        const tvShowEl = document.createElement("div");
        tvShowEl.classList.add("movie");
		const voteImage = getVoteImage(vote_average);

        tvShowEl.innerHTML = `
            <img src="${poster_path ? IMG_URL + poster_path : "http://via.placeholder.com/1080x1580"}" alt="${name}" class="movie-img">
            <div class="movie-info">
                <h3>${name}</h3>
                <img src="${voteImage}" alt="Vote: ${vote_average}" class="vote-image">
            </div>
            <div class="overview">
                <h3>Overview</h3>
                ${overview}
                <br/>
                <button class="recensisci" onclick="redirectToReviewPage(${id}, '${name}', 'tv')">Recensisci</button>
            </div>
        `;

        main.appendChild(tvShowEl);
    });
}

// Reindirizza alla pagina di dettagli della serie TV
function redirectToReviewPage(tvId, name, tipo) {
    window.location.href = `scheda.jsp?tvId=${tvId}&title=${encodeURIComponent(name)}&tipo=${tipo}`;
}

// Colore del voto
/*
function getColor(vote) {
    if (vote >= 8) {
        return "green";
    } else if (vote >= 5) {
        return "orange";
    } else {
        return "red";
    }
}*/

// Immagine inerente al voto
function getVoteImage(vote) {
  if (vote >= 8) {
    return "immagini/icontrash1.png";
  } else if (vote >= 5) {
    return "immagini/icontrash2.png";
  } else {
    return "immagini/icontrash3.png";
  }
}

// Funzionalità di ricerca
form.addEventListener("submit", (e) => {
    e.preventDefault();

    const searchTerm = search.value.trim();
    selectedGenre = [];
    setGenre();
    if (searchTerm) {
        getTVShows(searchURL + "&query=" + searchTerm);
    } else {
        getTVShows(API_URL);
    }
});