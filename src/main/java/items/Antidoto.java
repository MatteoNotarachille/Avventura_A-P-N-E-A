package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Antidoto è l'oggetto chiave del gioco. E' necessario per accedere e 
determinare, con la scelta su come usarlo, il finale della storia.

@author DeepCoders
*/
public class Antidoto extends Item {
    public Antidoto() {
        super("Antidoto");
    }

    @Override
    public void usa(Player player) {
        JOptionPane.showMessageDialog(null,
            "Hai usato "
            + getnome());
    }
}
