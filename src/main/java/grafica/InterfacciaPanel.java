package grafica;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Font;
import player.Player;

/*
Pannello posizionato nella fascia inferiore della finestra.
Mostra in tempo reale la salute del giocatore, l'esperienza accumulata e il contenuto
dell'inventario. Viene aggiornato ogni volta che lo stato del giocatore cambia.
 
@author DeepCoders
*/
public class InterfacciaPanel extends JPanel {
    private Player player;
    private JProgressBar hpBar;
    private JProgressBar expBar;
    private JLabel infoLabel;

    public InterfacciaPanel(Player player) {

        this.player = player;

        setLayout(null);
        setBackground(new Color(20, 20, 20));
        setBounds(0, 620, 1280, 100);   // fascia in basso nella finestra 1280x720
        
        // Etichetta e barra HP
        JLabel hpLabel = new JLabel("HP");
        hpLabel.setForeground(Color.WHITE);
        hpLabel.setFont(new Font("Arial", Font.BOLD, 16));
        hpLabel.setBounds(20, 15, 50, 20);
        add(hpLabel);

        hpBar = new JProgressBar(0, 100);
        hpBar.setValue(player.getsalute());
        hpBar.setBounds(70, 15, 250, 25);
        add(hpBar);
        
        // Etichetta e barra EXP
        JLabel expLabel = new JLabel("EXP");
        expLabel.setForeground(Color.WHITE);
        expLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expLabel.setBounds(350, 15, 50, 20);
        add(expLabel);

        expBar = new JProgressBar(0, 75);
        expBar.setValue(0);
        expBar.setBounds(410, 15, 250, 25);
        add(expBar);
        
        // Etichetta testuale con la lista degli oggetti in inventario
        infoLabel = new JLabel("Inventario: ");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        infoLabel.setBounds(20, 55, 1000, 25);
        add(infoLabel);
    }
    
    /*
    Aggiorna i valori mostrati leggendoli direttamente dal Player.
    Va chiamato ogni volta che HP, EXP o inventario cambiano.
    */
    public void updateInterfaccia() {
        hpBar.setValue(player.getsalute());
        expBar.setValue(player.getEsperienza());
        infoLabel.setText("Inventario: " + player.getInventario());
        repaint();
    }
}