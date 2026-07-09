package rete;

import java.io.*;
import java.net.*;

/*
Client socket invia il risultato della partita al server al termine del gioco.
Il messaggio contiene nome del giocatore, tipo di finale raggiunto e numero di morti.

@author DeepCoders
*/
public class SocketClient {
    private static String HOST = "localhost"; //host locale di deafult 
    private static final int PORTA = 12345;
    
    /*
    Invia il risultato al server tramite una connessione TCP.
    La connessione viene aperta, usata e chiusa in un unico blocco try-with-resources.
    */
    public static void inviaRisultato(String nome, String finale, int morti) {
        try (Socket socket = new Socket(HOST, PORTA);
            PrintWriter out = new PrintWriter(
                     socket.getOutputStream(), true)) {
            String risultato = nome + " | Finale: " + finale + " | Morti: " + morti;
            System.out.println("Invio risultato: " + risultato);
            out.println(risultato);
            System.out.println("Risultato inviato correttamente.");
        } catch (Exception e) {
            System.out.println("Errore invio risultato: " + e.getMessage());
        }
    }
    
    /*
    Imposta l'indirizzo IP del server a cui connettersi.
    Chiamato all'avvio quando il giocatore sceglie la modalità client.
    */
    public static void setHost(String ip) {
        HOST = ip;
    }
}
