package play;

import util.DialoghiManager;
import database.DatabaseManager;
import grafica.FinestraGioco;
import rete.SocketServer;
import rete.SocketClient;
import javax.swing.JOptionPane;

/*
* Classe principale del programma. Contiene il metodo main che avvia il gioco.
* 
* @author DeepCoders
*/
public class Play {

    public static void main(String[] args) {
        
        //Chiede all'utente se vuole avviare il server o connettersi come client
        String[] opzioni = {"Avvia come Server", "Connettiti come Client"};
        int scelta = JOptionPane.showOptionDialog(null,
            "Come vuoi giocare?",
            "A-P-N-E-A — Modalità",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, opzioni, opzioni[0]);
        
        //Se l'utente chiude il popup con X senza scegliere, chiude il programma
        if (scelta == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }
        
        if (scelta == 0) {
            //Fa da server
            SocketServer.avvia();   //avvia il server in background
        } else {
            //Fa da client, chiede l'IP del server
            String ip = (String) JOptionPane.showInputDialog(null,
                "Inserisci l'IP del computer server:",
                "Connessione",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "localhost");   //Valore di default
            if (ip == null || ip.isEmpty()) {
                System.exit(0);
            }
            
            //Imposta l'IP del server nel client socket
            SocketClient.setHost(ip);
        }
        
        //Carica tutti i dialoghi dal file di testo esterno
        DialoghiManager.carica();
        
        //Crea le tabelle del database se non esistono già
        DatabaseManager.createTables();
        
        //Avvia la finestra di gioco sull'EDT di Swing
        javax.swing.SwingUtilities.invokeLater(() -> new FinestraGioco());
    }
}
