package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;

/*
Gestisce la connessione e le operazioni sul database SQLite locale.
Salva le morti del giocatore e i finali raggiunti per tenere traccia
della partita. Tutti i metodi sono statici: non è necessario istanziare la classe.

@author DeepCoders
*/
public class DatabaseManager {
    
    // Percorso del file database SQLite
    private static final String URL = "jdbc:sqlite:A-P-N-E-A.db";
    
    //Apre e restituisce una connessione al database
    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }
    
    /*
    Crea le tabelle del database se non esistono ancora.
    Viene chiamata una sola volta all'avvio del programma.
    */
    public static void createTables() {

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            
            // Tabella per registrare ogni morte del giocatore con causa e data
            String deathsTable = """
                    CREATE TABLE IF NOT EXISTS deaths (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        player_name TEXT NOT NULL,
                        cause TEXT NOT NULL,
                        date TEXT DEFAULT CURRENT_TIMESTAMP
                    );
                    """;
            
            // Tabella per registrare i finali raggiunti
            String endingsTable = """
                    CREATE TABLE IF NOT EXISTS endings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        player_name TEXT NOT NULL,
                        ending_type TEXT NOT NULL,
                        date TEXT DEFAULT CURRENT_TIMESTAMP
                    );
                    """;

            stmt.execute(deathsTable);
            stmt.execute(endingsTable);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Errore database: " + e.getMessage());
        }
    }
    
    /*
    * Salva una morte nel database, registrando il nome del giocatore e la causa.
    * Usa PreparedStatement per evitare problemi con caratteri speciali nel testo.
    */
    public static void saveDeath(String playerName, String cause) {

        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO deaths(player_name, cause) VALUES(?, ?)")){ 
            
            stmt.setString(1, playerName);
            stmt.setString(2, cause);
            
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Errore salvataggio morte: " + e.getMessage());
        }
    }
    
    /*
    Salva il finale raggiunto nel database.
    Viene chiamata al termine della partita, indipendentemente dall'esito.
    */
    public static void saveEnding(String playerName, String endingType) {

        try (Connection conn = connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO endings(player_name, ending_type) VALUES(?, ?)")){
            
            stmt.setString(1, playerName);
            stmt.setString(2, endingType);

            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("Errore salvataggio finale: " + e.getMessage());
        }
    }
}
