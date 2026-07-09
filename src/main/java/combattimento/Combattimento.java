package combattimento;

import nemico.Nemico;
import player.Player;
import javax.swing.JOptionPane;
import util.DialoghiManager;

/*
Gestisce la logica del combattimento a turni tra il giocatore e un nemico.
Il combattimento continua finché uno dei due non è più in vita.
Se il giocatore rifiuta di attaccare per due turni consecutivi,
il nemico lo colpisce automaticamente come penalità.

@author DeepCoders
*/

public class Combattimento {
    
    /*
    Avvia e gestisce un combattimento completo.
    Restiruisce true se il giocatore ha vinto, false se è stato sconfitto
    */
    public static boolean inizioFight(Player player, Nemico nemico) {
        int rifiutiConsecutivi = 0;
        
        while(player.isAlive() && nemico.isAlive()) {
            
            // Mostra lo stato attuale e chiede al giocatore se attaccare
            int scelta = JOptionPane.showConfirmDialog(
                    null,
                    "Nemico: " + nemico.getnome()
                    + "\nHP nemico: " + nemico.getsalute()
                    + "\n\nI tuoi HP: " + player.getsalute()
                    + "\n\nVuoi attaccare?",
                    "Combattimento",
                    JOptionPane.YES_NO_OPTION
            );

            if(scelta == JOptionPane.YES_OPTION) {
                rifiutiConsecutivi = 0;
                
                // Il giocatore colpisce il nemico
                nemico.subisciDanno(player.getdanno());
                
                JOptionPane.showMessageDialog(
                        null,
                        "Hai colpito " + nemico.getnome()
                        + " infliggendo " + player.getdanno() + " danni."
                );

                // Se il nemico è ancora vivo, contrattacca
                if (nemico.isAlive()) {
                    int dannoNemico = nemico.getdanno();
                    player.subisciDanno(dannoNemico);

                    JOptionPane.showMessageDialog(
                            null,
                            nemico.getnome() + " ti ha colpito.\n"
                            + "Hai perso " + dannoNemico + " HP."
                    );
                }
            } else {
                rifiutiConsecutivi++;
                
                if (rifiutiConsecutivi >= 2) {
                    // Dopo 2 rifiuti il nemico colpisce comunque senza difesa da parte del player
                    JOptionPane.showMessageDialog(null,
                        "Hai esitato troppo!\n"
                        + nemico.getnome() + " ne approfitta e ti attacca.");
                    
                    int dannoNemico = nemico.getdanno();
                    player.subisciDanno(dannoNemico);
                    JOptionPane.showMessageDialog(null,
                        nemico.getnome() + " ti ha colpito.\n"
                        + "Hai perso " + dannoNemico + " HP.");

                    rifiutiConsecutivi = 0;   // Reset dopo la penalità
                }else {
                    JOptionPane.showMessageDialog(null, DialoghiManager.get("COMBAT_SCAPPA"));
                } 
            }
        }
        
        // Determina e restituisce il risultato
        if(player.isAlive()) {
            JOptionPane.showMessageDialog(null, "Hai sconfitto " + nemico.getnome());
            player.addesperienza(50);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, DialoghiManager.get("COMBAT_MORTE"));
            return false;
        }
    }
}
