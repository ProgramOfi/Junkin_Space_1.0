<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="navbar.css">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="homepage.css">
    <title>Junkin Space</title>
</head>

<body>
     
    <body>
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

<!--Contenuto Pagina-->
    <div class="hero">
        <img src="immagini/Junkin_Space3.gif" alt="Hero Image">
    </div>
    <div class="welcome">
        <div class="welcome_img">
            <img src="immagini/Logo_1.png" alt="welcome">
        </div>
      <div class="welcome_text">
            <div class="testo">
                Junkin Space è il punto di ritrovo ideale per gli amanti dei film che sfidano le convenzioni della "qualità".
                Qui puoi dare sfogo alla tua creatività con recensioni originali,
                perfette per chi sa apprezzare i film che sono così brutti da risultare irresistibili.
                Se cerchi un'ispirazione per una serata con gli amici all'insegna dell'assurdo,
                o vuoi condividere la tua opinione in modo scherzoso e fuori dagli schemi,
                sei nel posto giusto!
                Su Junkin Space, ogni film ha il suo momento di gloria, indipendentemente da ciò che ne pensa la critica.
                Unisciti alla nostra community e scopri il lato divertente del cinema che spesso passa inosservato.
                <br>
                <br>
                Ricorda:
            </div>
                <div class="pocotrash">
                    <img src="immagini/icontrash1.png" alt="pocotrash">
                    <h4>Niente Junkin</h4>
                </div>
                <div class="mediotrash">
                    <img src="immagini/icontrash2.png" alt="mediotrash">
                    <h4>Poco Junkin</h4>
                </div>
                <div class="moltotrash">
                    <img src="immagini/icontrash3.png" alt="moltotrash">
                    <h4>Molto Junkin</h4>
                </div>
           </div>
    </div>
    <section class="board">
        <div class="box" id="film">
            <div class="overlay"  onclick="window.location.href='film.jsp'">
                <p>Dai un occhio alle ultime recensioni</p>
            </div>
            <h3>Film</h3>
            <img src="immagini/film.jpg" alt="Film Image">
        </div>
        <div class="box" id="serie">
            <div class="overlay" onclick="window.location.href='serieTV.jsp'">
                <p>Dai un occhio alle ultime recensioni</p>
            </div>
            <h3>Serie TV</h3>
            <img src="immagini/serietv.jpg" alt="Serie TV Image">
        </div>
        <div class="box" id="anime">
            <div class="overlay" onclick="window.location.href='crediti.jsp'">
                <p>Work in progress</p>
            </div>
            <h3>Anime</h3>
            <img src="immagini/anime.jpg" alt="Anime Image">
        </div>
    </section>

<!--Footer-->
    <footer>
        <p>&copy; 2024 Junkin Space</p>
    </footer>

    <script src="homepage.js"></script>
</body>
</html>
