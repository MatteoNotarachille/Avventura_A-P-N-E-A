package grafica;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;

/*
Classe di utilità per caricare immagini dalle risorse del progetto.
Le immagini vengono cercate nel classpath tramite getResource(),
in modo da funzionare sia in sviluppo che nel JAR esportato.
In caso di errore restituisce un'immagine nera vuota per evitare crash.

@author DeepCoders
*/
public class GestisciImmagine {
    
    /*
    Carica un'immagine dal percorso indicato (relativo al classpath).
    path = percorso dell'immagine, es. "/assets/bunker/bunker_room.png"
    Restituisce l'immagine caricata, oppure un'immagine vuota
    */
     public static Image load(String path) {
        try {
            java.net.URL url = GestisciImmagine.class.getResource(path);
            if (url == null) {
                System.out.println("Immagine non trovata: " + path);
                return new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
            }
            return new ImageIcon(url).getImage();
        } catch (Exception e) {
            System.out.println("Errore caricamento immagine: " + path);
            return new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
        }
    }
}