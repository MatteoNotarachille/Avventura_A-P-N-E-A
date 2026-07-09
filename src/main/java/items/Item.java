package items;

import player.Player;

/*
Classe astratta che rappresenta un generico oggetto raccoglibile nel gioco.
Ogni item ha un nome e un comportamento specifico quando viene usato,
definito dalle sottoclassi tramite il metodo astratto usa().

@author DeepCoders
*/
public abstract class Item {
    protected String nome;

    public Item(String nome) {
        this.nome = nome;
    }

    public String getnome() {
        return nome;
    }
    
    /**
    * Applica l'effetto dell'oggetto sul giocatore.
    */
    public abstract void usa(Player player);
}
