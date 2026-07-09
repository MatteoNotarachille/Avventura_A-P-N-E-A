package player;

import items.Item;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import util.DialoghiManager;

/*
Rappresenta il giocatore. Gestisce tutte le informazioni sullo stato del personaggio:
salute, esperienza, danno, livello e inventario degli oggetti raccolti.

@author DeepCoders
*/
public class Player {
    private String nome;
    private int salute;
    private int maxSalute;
    private int esperienza;
    private int danno;
    private int levelup;    // bonus di danno accumulato con i level-up
    private int contatoreMorti;

    private ArrayList<Item> inventario;

    /*
    Crea un nuovo giocatore con i valori di partenza predefiniti.
    nome = il nome scelto dal giocatore all'avvio
    */
    public Player(String nome) {
        this.nome = nome;
        this.salute = 80;
        this.maxSalute = 100;
        this.esperienza = 0;
        this.danno = 10;
        this.levelup = 0;
        this.contatoreMorti = 0;

        inventario = new ArrayList<>();
    }
    
    /*
    Aggiunge un oggetto all'inventario e mostra un messaggio al giocatore.
    */
    public void addItem(Item item) {
        inventario.add(item);
        JOptionPane.showMessageDialog(null, item.getnome() + " aggiunto all'inventario.");
    }
    
    //Rimuove un item dall'inventario dopo averlo usato
    public void removeItem(Item item) {
        inventario.remove(item);
    }
    
    /*
    Controlla se nell'inventario è presente un oggetto con il nome specificato.
    */
    public boolean hasItem(String itemNome) {

        for (Item item : inventario) {
            if (item.getnome().equalsIgnoreCase(itemNome)) {
                return true;
            }
        }

        return false;
    }

    /*
    Aumenta la salute del giocatore della quantità indicata,
    senza superare il valore massimo.
    */
    public void curare(int quantita) {
        salute += quantita;

        if(salute > maxSalute) {
            salute = maxSalute;
        }
    }
    
    /*
    Riduce la salute del giocatore della quantità indicata.
    La salute non scende sotto zero.
    */
    public void subisciDanno(int quantita) {
        salute -= quantita;

        if(salute <= 0) {
            salute = 0;
        }
    }

    /*
    Aggiunge esperienza al giocatore. Quando si raggiungono 75 punti
    scatta il level-up: il danno base aumenta di 5 e l'esperienza viene azzerata.
    */
    public void addesperienza(int quantita) {
        esperienza += quantita;

        if(esperienza >= 75) {
            levelup += 5;
            danno += 5;
            esperienza = 0;
            JOptionPane.showMessageDialog(null,
                DialoghiManager.get("LEVELUP"));
        }
    }
    
    /*
    Reimposta il giocatore allo stato iniziale dopo una morte.
    Azzera salute, esperienza, danno, bonus di livello e svuota l'inventario.
    */
    public void reset() {
        this.salute = 80;
        this.esperienza = 0;
        this.danno = 10;
        this.levelup = 0;
        this.inventario.clear();
    }
    
    /*
    Incrementa il contatore delle morti del giocatore.
    */
    public void incrementaMorti() {
        contatoreMorti++;
    }   
    
    /*
    Restituisce l'inventario come stringa leggibile, 
    con gli item separati da " | ".
    Se l'inventario è vuoto restituisce "vuoto".
    */
    public String getInventario() {

        if (inventario.isEmpty()) {
            return "vuoto";
        }

        return inventario.stream()
            .map(Item::getnome)
            .collect(java.util.stream.Collectors.joining(" | "));
    }
    
    /*
    Restituisce la lista vera e propria degli item, usata quando
    si deve iterare sugli oggetti (es. per usare il medikit).
    */
    public ArrayList<Item> getInventarioLista() {
        return inventario;
    }
    
    public int getContatoreMorti() {
        return contatoreMorti;
    }   
    
    /*
    Imposta il danno del giocatore sommando il bonus da level-up.
    Viene chiamato quando si equipaggia un'arma.
    */
    public void setDanno(int danno) {
        this.danno = danno + levelup;
    }
    
    public int getdanno() {
        return danno;
    }

    public int getsalute() {
        return salute;
    }

    public String getnome() {
        return nome;
    }

    public boolean isAlive() {
        return salute > 0;
    }
    
    public int getEsperienza() { 
        return esperienza; 
    }
}
