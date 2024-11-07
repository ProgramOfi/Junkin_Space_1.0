package dao;

import model.Recensione;
import model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class RecensioneDao {

    // Metodo per inserire una nuova recensione
    public static boolean insertRecensione(Recensione recensione, Connection connection) {
        boolean result = false;
        String query = "INSERT INTO recensioni (tmdb_id, utente_id, commento, data_recensione, tipo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, recensione.getTmdbFilmId());  // Inserisce il `tmdb_id`
            pst.setInt(2, recensione.getUtente().getUtenteId());  // Inserisce l'ID dell'utente
            pst.setString(3, recensione.getCommento());  // Inserisce il commento
            pst.setTimestamp(4, Timestamp.valueOf(recensione.getDataRecensione()));  // Inserisce la data
            pst.setString(5, recensione.getTipo());  // Inserisce il tipo (film, serie, anime)
            pst.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Metodo per ottenere una recensione per ID
    public static Recensione getRecensione(int id, Connection connection) {
        Recensione r = null;
        String query = "SELECT r.recensione_id, r.tmdb_id, r.utente_id, r.commento, r.data_recensione, r.tipo, "
                     + "u.username, u.email "
                     + "FROM recensioni r JOIN utenti u ON r.utente_id = u.utente_id "
                     + "WHERE r.recensione_id = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    r = new Recensione();
                    r.setRecensioneId(rs.getInt("recensione_id"));
                    r.setTmdbFilmId(rs.getString("tmdb_id"));
                    r.setCommento(rs.getString("commento"));
                    r.setTipo(rs.getString("tipo"));
                    r.setDataRecensione(rs.getTimestamp("data_recensione").toLocalDateTime());
                   
                    

                    // Creazione oggetto Utente
                    Utente u = new Utente();
                    u.setUtenteId(rs.getInt("utente_id"));
                    u.setUsername(rs.getString("username"));
                    r.setUtente(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    // Metodo per ottenere tutte le recensioni filtrate per tmdb_id e tipo
    public static List<Recensione> getRecensioniByTmdbIdAndTipo(String tmdbId, String tipo, Connection connection) {
        List<Recensione> recensioni = new ArrayList<>();
        String query = "SELECT r.recensione_id, r.tmdb_id, r.utente_id, r.commento, r.data_recensione, r.tipo, "
                     + "u.username, u.email "
                     + "FROM recensioni r JOIN utenti u ON r.utente_id = u.utente_id "
                     + "WHERE r.tmdb_id = ? AND r.tipo = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, tmdbId);
            pst.setString(2, tipo);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Recensione r = new Recensione();
                    r.setRecensioneId(rs.getInt("recensione_id"));
                    r.setTmdbFilmId(rs.getString("tmdb_id"));
                    r.setCommento(rs.getString("commento"));
                    r.setTipo(rs.getString("tipo"));

                    // Recupera la data come java.sql.Date
                    r.setDataRecensione(rs.getTimestamp("data_recensione").toLocalDateTime());

                    // Creazione oggetto Utente
                    Utente u = new Utente();
                    u.setUtenteId(rs.getInt("utente_id"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    r.setUtente(u);

                    recensioni.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recensioni;
    }

    // Metodo per aggiornare una recensione
    public static boolean updateRecensione(int recensioneId, Recensione recensioneAggiornata, Connection connection) {
        boolean aggiornato = false;
        String query = "UPDATE recensioni SET tmdb_id = ?, utente_id = ?, commento = ?, data_recensione = ?, tipo = ? WHERE recensione_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, recensioneAggiornata.getTmdbFilmId());
            pst.setInt(2, recensioneAggiornata.getUtente().getUtenteId());
            pst.setString(3, recensioneAggiornata.getCommento());

            // Aggiorna la data della recensione come java.sql.Date
            if (recensioneAggiornata.getDataRecensione() != null) {
            	pst.setTimestamp(4, Timestamp.valueOf(recensioneAggiornata.getDataRecensione()));
            } else {
                pst.setNull(4, java.sql.Types.DATE);
            }

            pst.setString(5, recensioneAggiornata.getTipo());
            pst.setInt(6, recensioneId);

            // Esegui l'update
            int rowsAffected = pst.executeUpdate();
            aggiornato = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aggiornato;
    }

    // Metodo per eliminare una recensione
    public static boolean deleteRecensione(int recensioneId, Connection connection) {
        boolean cancellato = false;
        String query = "DELETE FROM recensioni WHERE recensione_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, recensioneId);
            int rowsAffected = pst.executeUpdate();
            cancellato = (rowsAffected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cancellato;
    }
}