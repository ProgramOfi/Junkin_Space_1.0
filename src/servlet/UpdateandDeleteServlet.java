package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import connessione.ConnessioneDb;
import dao.RecensioneDao;
import model.Recensione;
import model.Utente;

@WebServlet("/gestioneRecensione")
public class UpdateandDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utenteLoggato") : null;

        if (utente == null) {
            response.sendRedirect("loginregister.jsp");
            return;
        }

        // Parametro che indica l'azione da eseguire: "modifica" o "cancella"
        String action = request.getParameter("action");
        String recensioneIdStr = request.getParameter("recensioneId");
        String tmdbFilmId = request.getParameter("tmdbFilmId");

        if (recensioneIdStr != null && tmdbFilmId != null && action != null) {
            int recensioneId = Integer.parseInt(recensioneIdStr);

            try (Connection connection = ConnessioneDb.getCon()) {
                if (action.equals("modifica")) {
                    // Modifica recensione
                    String commento = request.getParameter("commento");
                    String dataRecensioneStr = request.getParameter("dataRecensione"); // Supponiamo che tu riceva la data come stringa

                    if (commento != null && !commento.isEmpty() && dataRecensioneStr != null && !dataRecensioneStr.isEmpty()) {
                        // Conversione della stringa in LocalDateTime
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // Definisci il pattern
                        LocalDateTime dataRecensione = LocalDateTime.parse(dataRecensioneStr, formatter);

                        Recensione recensioneAggiornata = new Recensione();
                        recensioneAggiornata.setTmdbFilmId(tmdbFilmId);
                        recensioneAggiornata.setUtente(utente);
                        recensioneAggiornata.setCommento(commento);
                        recensioneAggiornata.setDataRecensione(dataRecensione); // Imposta LocalDateTime

                        boolean aggiornata = RecensioneDao.updateRecensione(recensioneId, recensioneAggiornata, connection);
                        if (aggiornata) {
                            System.out.println("Recensione aggiornata con successo.");
                        } else {
                            System.out.println("Errore durante l'aggiornamento della recensione.");
                        }
                    } else {
                        System.out.println("Parametri non validi per la modifica della recensione.");
                    }
                } else if (action.equals("cancella")) {
                    // Cancella recensione
                    boolean cancellata = RecensioneDao.deleteRecensione(recensioneId, connection);
                    if (cancellata) {
                        System.out.println("Recensione cancellata con successo.");
                    } else {
                        System.out.println("Errore durante la cancellazione della recensione.");
                    }
                } else {
                    System.out.println("Azione non riconosciuta.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Parametri non validi per l'operazione.");
        }

        // Redirect alla pagina del film per vedere l'aggiornamento
        response.sendRedirect("scheda.jsp?movieId=" + tmdbFilmId);
    }
}