package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.RecensioneDao;
import model.Recensione;
import model.Utente;
import connessione.ConnessioneDb;

@WebServlet("/aggiungiRecensione")
public class AggiungiRecensioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utenteLoggato") : null;

        if (utente == null) {
            response.sendRedirect("loginregister.jsp");
            return;
        }

        String commento = request.getParameter("commento");
        String tmdbId = request.getParameter("tmdb_id");
        String tipo = request.getParameter("tipo");

        if (commento != null && !commento.isEmpty() && tmdbId != null && !tmdbId.isEmpty()) {
            try (Connection connection = ConnessioneDb.getCon()) {
                Recensione nuovaRecensione = new Recensione();
                nuovaRecensione.setTmdbFilmId(tmdbId);
                nuovaRecensione.setUtente(utente);
                nuovaRecensione.setCommento(commento);
                nuovaRecensione.setTipo(tipo);

                // Imposta la data e ora attuale per la recensione
                nuovaRecensione.setDataRecensione(LocalDateTime.now());

                boolean inserita = RecensioneDao.insertRecensione(nuovaRecensione, connection);

                if (inserita) {
                    System.out.println("Recensione inserita con successo.");
                } else {
                    System.out.println("Errore durante l'inserimento della recensione.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Parametri mancanti per l'inserimento della recensione.");
        }

        // Reindirizza alla pagina del film dopo l'inserimento
        response.sendRedirect("scheda.jsp?movieId=" + tmdbId);
    }
}
