package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Kit medico. Quando usato ripristina completamente la salute del giocatore.

@author DeepCoders
*/
public class Medikit extends Item {
    public Medikit() {
        super("Medikit");
    }

    @Override
    public void usa(Player player) {
        player.curare(100);
        JOptionPane.showMessageDialog(null,
            "Hai usato "
            + getnome()
            + ".\nVita completamente rigenerata.");
    }
}
