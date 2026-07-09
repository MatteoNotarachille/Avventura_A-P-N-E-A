package grafica;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import player.Player;
import ui.ConsoleUI;

/*
Finestra principale del gioco. Si occupa di mostrare la schermata titolo
e poi avvia il pannello di gioco vero e proprio.

@author DeepCoders
*/
public class FinestraGioco extends JFrame {
    
    private Player player;
    
    public FinestraGioco() {
        setTitle("A-P-N-E-A");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // imposta la dimensione dell'area di gioco sul contentPane, non sul JFrame stesso
        getContentPane().setPreferredSize(new Dimension(1280, 720));
        pack();   // calcola la dimensione esterna corretta in base al contenuto interno
        
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Prima mostra la schermata titolo
        ConsoleUI.mostraTitolo(this, () -> avviaGioco());
    }
    
    /*
    Chiede il nome al giocatore, crea il Player e sostituisce
    la schermata titolo con il pannello di gioco.
    */
    private void avviaGioco() {
        String nomeGiocatore = JOptionPane.showInputDialog(this,
        "Inserisci il tuo nome:",
        "Benvenuto in A-P-N-E-A",
        JOptionPane.QUESTION_MESSAGE);
        
        // Se il campo è vuoto o l'utente chiude il popup, usa "Anonimo"
        if (nomeGiocatore == null || nomeGiocatore.trim().isEmpty()) {
            nomeGiocatore = "Anonimo";
        }
        
        player = new Player(nomeGiocatore.trim());

        // Rimuove la schermata titolo
        getContentPane().removeAll();
        
        // Aggiunge il pannello di gioco
        ScenePanel scenePanel = new ScenePanel(player, this);
        add(scenePanel);

        revalidate();
        repaint();
    }
}