package util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
Timer a countdown usato nel laboratorio chimico.
Quando il tempo scade viene mostrato un messaggio e viene eseguita
l'azione di callback passata dal chiamante (la morte per gas).

@author DeepCoders
*/
public class LabTimer {
    private int secondi;
    private Timer timer;

    /*
    Crea il timer
    onTimeExpired = azione da eseguire quando il tempo finisce
    */
    public LabTimer(int seconds, Runnable onTimeExpired) {

        this.secondi = seconds;
        
        // Ogni secondo decrementa il contatore; quando arriva a zero esegue il callback
        timer = new Timer(1000, e -> {
            this.secondi--;

            if (this.secondi < 0) {
                timer.stop();
                JOptionPane.showMessageDialog(null, "Tempo scaduto!");
                onTimeExpired.run();
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
