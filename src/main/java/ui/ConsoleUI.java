package ui;

import grafica.GestisciImmagine;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
Pannello della schermata titolo. Mostra l'immagine di copertina del gioco
e avanza al click del mouse, eseguendo il callback passato dalla finestra principale.

@author DeepCoders
*/
public class ConsoleUI extends JPanel{
    
    private Image imgTitolo;
    private Runnable onStart;   //azione da eseguire quando il giocatore clicca per iniziare
    
    public ConsoleUI(Runnable onStart) {
        this.onStart = onStart;
        imgTitolo = GestisciImmagine.load("/assets/title_screen.png");
        
        // Al click ovunque sul pannello avvia il gioco
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onStart.run();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imgTitolo, 0, 0, getWidth(), getHeight(), null);
    }
    
    /*
    Metodo statico di comodo: aggiunge la schermata titolo alla finestra indicata.
    */
    public static void mostraTitolo(JFrame finestra, Runnable onStart) {
        ConsoleUI schermataInizio = new ConsoleUI(onStart);
        finestra.add(schermataInizio);
        finestra.revalidate();
        finestra.repaint();
    }
}
