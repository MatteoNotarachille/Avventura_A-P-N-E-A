package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/*
Carica e fornisce i testi di gioco (dialoghi, messaggi, notifiche) da un file esterno.
testi sono salvati in dialoghi.txt nel formato CHIAVE=valore,
così possono essere modificati senza toccare il codice sorgente.

@author DeepCoders
*/

public class DialoghiManager {
    
    // Mappa che associa ogni chiave al testo corrispondente
    private static HashMap<String, String> dialoghi = new HashMap<>();

    /*
    Legge il file e popola la mappa.
    Va chiamata una sola volta all'avvio, prima di usare get().
    */
    public static void carica() {
        try (BufferedReader br = new BufferedReader(new FileReader("dialoghi.txt"))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (riga.contains("=")) {
                    
                    // Divide solo sul primo "=" per supportare valori che contengono "="
                    String[] parti = riga.split("=", 2);
                    dialoghi.put(parti[0].trim(), parti[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println("Errore caricamento dialoghi: " + e.getMessage());
        }
    }

    
    /*
    Restituisce il testo associato alla chiave indicata.
    Converte il carattere di \n in un vero a capo.
    Se la chiave non esiste restituisce "..." come testo di fallback.
    */
    public static String get(String chiave) {
        return dialoghi.getOrDefault(chiave, "...").replace("\\n", "\n");
    }
}
