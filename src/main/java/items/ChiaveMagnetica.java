package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Chiave magnetica trovata nella scrivania del direttore.
Viene usata per aprire la cassaforte che contiene l'antidoto.

@author DeepCoders
*/
public class ChiaveMagnetica extends Item {
    public ChiaveMagnetica() {
        super("Chiave magnetica");
    }

    @Override
    public void usa(Player player) {
        JOptionPane.showMessageDialog(null,
            "Hai usato " 
            + getnome());
    }
}
