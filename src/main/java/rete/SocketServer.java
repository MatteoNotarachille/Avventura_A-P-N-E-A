package rete;

import java.io.*;
import java.net.*;
import java.util.*;

/*
Server socket riceve i risultati delle partite degli altri giocatori.
Viene avviato in un thread separato così da non bloccare l'interfaccia grafica.
Per ogni client che si connette crea un thread dedicato alla gestione.
Dopo ogni ricezione aggiorna il file classifica.txt ordinato per numero di morti.

@author DeepCoders
*/
public class SocketServer {
    private static final int PORTA = 12345;
    
    // Lista condivisa dei risultati ricevuti da tutti i client
    private static List<String> risultati = new ArrayList<>();
    
    /*
    Avvia il server in ascolto sulla porta definita.
    Usa setReuseAddress per evitare errori se la porta è ancora in TIME_WAIT.
    */
    public static void avvia() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true); 
                serverSocket.bind(new java.net.InetSocketAddress(PORTA));
                System.out.println("Server avviato sulla porta " + PORTA);
                
                // Loop infinito: accetta connessioni da nuovi client
                while (true) {
                    Socket client = serverSocket.accept();
                    System.out.println("Client connesso!");
                    gestisciClient(client);
                }
            } catch (Exception e) {
                System.out.println("Errore server: " + e.getMessage());
            }
        }).start();
    }

    /*
    Gestisce la comunicazione con un singolo client.
    Legge una riga di testo col risultato e aggiorna la classifica.
    */
    private static void gestisciClient(Socket client) {
        new Thread(() -> {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()))) {
                System.out.println("Client connesso: " + client.getInetAddress());
                String risultato = in.readLine();
                System.out.println("Risultato ricevuto: " + risultato);
                risultati.add(risultato);
                stampaClassifica();
            } catch (Exception e) {
                System.out.println("Errore client: " + e.getMessage());
            }
        }).start();
    }
    
    /*
    Ordina i risultati per numero di morti e sovrascrive il file 
    classifica.txt ad ogni aggiornamento per mostrare sempre la versione corrente.
    */
    private static void stampaClassifica() {
        // ordina per numero di morti crescente
        risultati.sort((a, b) -> {
            int mortiA = estraiMorti(a);
            int mortiB = estraiMorti(b);
            return Integer.compare(mortiA, mortiB);
        });

        // scrive su file
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("classifica.txt", false))) {
            bw.write("=== CLASSIFICA A-P-N-E-A ===");
            bw.newLine();
            bw.write("Aggiornata: " + 
                java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")));
            bw.newLine();
            bw.write("============================");
            bw.newLine();
            int posizione = 1;
            for (String r : risultati) {
                bw.write(posizione + ". " + r);
                bw.newLine();
                posizione++;
            }
        } catch (Exception e) {
            System.out.println("Errore scrittura classifica: " + e.getMessage());
        }
    }
    
    /*
    Estrae il numero di morti dalla stringa risultato.
    In caso di errore di parsing restituisce 999 per mettere il record in fondo.
    */
    private static int estraiMorti(String risultato) {
        try {
            String[] parti = risultato.split("Morti: ");
            return Integer.parseInt(parti[1].trim());
        } catch (Exception e) {
            return 999;
        }
    }
}
