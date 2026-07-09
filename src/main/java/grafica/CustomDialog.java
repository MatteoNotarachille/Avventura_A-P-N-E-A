package grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
Finestra di dialogo personalizzata usata per messaggi narrativi del gioco.
Ha uno stile coerente con l'estetica del gioco: sfondo scuro, testo bianco
e bordo rosso. Sostituisce il JOptionPane standard per i messaggi più importanti.

@author DeepCoders
*/

public class CustomDialog extends JDialog{
    
    /*
    Crea e mostra immediatamente il dialogo.
    parent = la finestra principale a cui agganciarsi
    messaggio = il testo da mostrare
    */
    public CustomDialog(JFrame parent, String messaggio) {
        super(parent, true);    // true = modale, blocca la finestra parent
        setUndecorated(true);   // rimuove la barra del titolo di sistema
        setSize(600, 300);
        setLocationRelativeTo(parent);
        
        // Pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createLineBorder(new Color(180, 0, 0), 3));
        
        // Etichetta con il testo del messaggio, supporta HTML per andare a capo
        JLabel label = new JLabel("<html><center>" + messaggio.replace("\n", "<br>") + "</center></html>");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        
        // Pulsante per chiudere il dialogo e continuare
        JButton okButton = new JButton("CONTINUA");
        okButton.setBackground(new Color(180, 0, 0));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(200, 45));

        okButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buttonPanel.add(okButton);

        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
    
    /*
    Metodo statico di comodo per creare e mostrare il dialogo in una riga sola.
    */
    public static void mostra(JFrame parent, String messaggio) {
        new CustomDialog(parent, messaggio);
    }
}
