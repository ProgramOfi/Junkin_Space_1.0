package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.RecensioneDao;
import model.Recensione;
import model.LocalDateTimeAdapter; // Importa il tuo adapter
import connessione.ConnessioneDb;

@WebServlet("/getRecensioni")
public class GetRecensioniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tmdbId = request.getParameter("tmdb_id");
        String tipo = request.getParameter("tipo");

        try (Connection connection = ConnessioneDb.getCon()) {
            List<Recensione> recensioni = RecensioneDao.getRecensioniByTmdbIdAndTipo(tmdbId, tipo, connection);

            // Usa un GsonBuilder e registra il LocalDateTimeAdapter
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // Registra l'adapter
                .create();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Serializza le recensioni in formato JSON
            String jsonRecensioni = gson.toJson(recensioni);
            response.getWriter().write(jsonRecensioni);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero delle recensioni.");
        }
    }
}