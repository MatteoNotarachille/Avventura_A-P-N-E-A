package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
Genera un file di testo con il riepilogo della partita appena conclusa.
Il file report_partita.txt viene riscritto ad ogni chiamata,
conservando sempre i dati dell'ultima partita giocata.

@author DeepCoders
*/
public class ReportManager {
    
    /*
    Scrive il report della partita su file.
    nome = nome del giocatore
    finale = tipo di finale raggiunto
    morti = numero totale di morti durante la partita
    */
    public static void salvaReport(String nome, String finale, int morti) {
        String nomeFile = "report_partita.txt";
        String data = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeFile, false))) {
            bw.write("=============================");
            bw.newLine();
            bw.write("     A-P-N-E-A — REPORT");
            bw.newLine();
            bw.write("=============================");
            bw.newLine();
            bw.write("Giocatore: " + nome);
            bw.newLine();
            bw.write("Data partita: " + data);
            bw.newLine();
            bw.write("Finale ottenuto: " + finale);
            bw.newLine();
            bw.write("Numero di morti: " + morti);
            bw.newLine();
            bw.write("=============================");
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Errore scrittura report: " + e.getMessage());
        }
    }
}
