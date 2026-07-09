package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Oggetto cibo. Quando usato ripristina 20 HP al giocatore.

@author DeepCoders
*/
public class Cibo extends Item {
    public Cibo() {
        super("Cibo");
    }

    @Override
    public void usa(Player player) {
        player.curare(20);
        JOptionPane.showMessageDialog(null,
            "Hai mangiato del " 
            + getnome()
            + ".\nHai recuperato 20 HP.");
    }
}
