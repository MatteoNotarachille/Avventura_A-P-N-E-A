package nemico;

/*
Rappresenta un nemico affrontato durante il combattimento.
Ogni nemico ha un nome, una quantità di salute e un valore di danno.
Le istanze vengono create dinamicamente da ScenePanel per ogni scontro.

@author DeepCoders
*/
public class Nemico {
    private String nome;
    private int salute;
    private int danno;

    public Nemico(String nome, int salute, int danno) {
        this.nome = nome;
        this.salute = salute;
        this.danno = danno;
    }
    
    /*
    Riduce la salute del nemico della quantità indicata.
    La salute non scende sotto zero.
    */
    public void subisciDanno(int quantita) {
        salute -= quantita;

        if(salute < 0) {
            salute = 0;
        }
    }

    public boolean isAlive() {
        return salute > 0;
    }

    public String getnome() {
        return nome;
    }

    public int getdanno() {
        return danno;
    }

    public int getsalute() {
        return salute;
    }
}
