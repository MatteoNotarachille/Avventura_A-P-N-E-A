package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Fusibile trovato nella metropolitana. Il suo uso è narrativo:
non modifica le statistiche del giocatore ma avanza la storia.

@author DeepCoders
*/
public class Fusibile extends Item {
    public Fusibile() {
        super("Fusibile");
    }
    
    @Override
    public void usa(Player player) {
        JOptionPane.showMessageDialog(null,
            "Hai usato "
            + getnome());
    }
}
