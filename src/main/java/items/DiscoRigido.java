package items;

import javax.swing.JOptionPane;
import player.Player;

/*
Disco rigido da inserire nel PC del laboratorio chimico.
Serve per recuperare i file necessari ad avanzare nella storia.

@author DeepCoders
*/
public class DiscoRigido extends Item {
    public DiscoRigido() {
        super("Disco rigido");
    }

    @Override
    public void usa(Player player) {
        JOptionPane.showMessageDialog(null,
            "Hai inserito "
            + getnome()
            + " nel PC.");
    }
}
