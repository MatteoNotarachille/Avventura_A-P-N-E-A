package items;

import player.Player;
import javax.swing.JOptionPane;

/*
Arma equipaggiabile. Quando usata, aggiorna il valore di danno del giocatore
con quello specifico dell'arma, sommando eventuali bonus di livello.

@author DeepCoders
*/
public class Arma extends Item {
    private int danno;

    public Arma(String nome, int danno) {
        super(nome);
        this.danno = danno;
    }

    @Override
    public void usa(Player player) {
        player.setDanno(danno);
        JOptionPane.showMessageDialog(null,
            "Arma equipaggiata: " 
            + getnome()
            + "\nDanno aumentato a " 
            + player.getdanno() 
            + ".");
    }
}