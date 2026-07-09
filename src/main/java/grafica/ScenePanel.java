package grafica;

import javax.swing.JLabel;
import javax.swing.JFrame;
import util.LabTimer;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import player.Player;
import items.Item;
import items.Cibo;
import items.Medikit;
import items.Arma;
import items.Fusibile;
import items.Antidoto;
import items.ChiaveMagnetica;
import items.DiscoRigido;
import combattimento.Combattimento;
import nemico.Nemico;
import api.RestCodeClient;
import database.DatabaseManager;
import util.DialoghiManager;
import util.ReportManager;
import rete.SocketClient;

/*
Pannello principale del gioco. Gestisce il rendering delle scene,
l'intercettazione dei click del mouse e tutta la logica narrativa e interattiva.
Ogni scena è identificata da una stringa (es. "BUNKER", "METRO") e i click
vengono forniti al metodo di interazione corretto tramite uno switch.
 
@author DeepCoders
*/
public class ScenePanel extends JPanel {
    private Image imgCorrente;  // immagine di sfondo della scena attiva
    private String scenaCorrente;   // identificatore testuale della scena attiva
    private Player player;
    private InterfacciaPanel interfacciaPanel;
    private String code;     // codice PIN generato dalla chiamata REST
    
    // Flag che tracciano lo stato del gioco per ogni sessione
    private boolean aperturaCassa1;
    private boolean usoTV;
    private boolean fusibileTrovato;
    private boolean soldatoConsultato; 
    private boolean aperturaCassa2;   
    private boolean chiaveTrovata;        
    private boolean finaleCompletato;
    private boolean codiceOttenuto;
    private boolean discoTrovato;
    private boolean pcUsato;
    
    private LabTimer labTimer;
    private boolean labTimerStarted;
    
    private JFrame frameParent;

    public ScenePanel(Player player, JFrame frameParent) {
        this.player = player;
        this.frameParent = frameParent;
        
        setLayout(null);
        
        // Scena iniziale: stanza del bunker
        scenaCorrente = "BUNKER";
        imgCorrente = GestisciImmagine.load("/assets/bunker/bunker_room.png");
        
        // Tutti i flag inizializzati: nessuna azione ancora compiuta
        aperturaCassa1 = false;
        usoTV = false;
        fusibileTrovato = false; 
        soldatoConsultato = false;
        aperturaCassa2 = false;
        chiaveTrovata = false;
        finaleCompletato = false;
        codiceOttenuto = false;
        pcUsato = false;
        
        interfacciaPanel = new InterfacciaPanel(player);
        add(interfacciaPanel);

        labTimerStarted = false;
        
        // Listener che cattura ogni click e lo passa alla logica di interazione
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                System.out.println("Click: " + x + ", " + y);
                
                interazioneClick(x, y);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Disegna l'immagine di sfondo scalata all'intera area del pannello
        g.drawImage(imgCorrente, 0, 0, getWidth(), getHeight(), null);
    }
    
    /*
    Cambia la scena attiva aggiornando l'identificatore e l'immagine di sfondo,
    poi forza il ridisegno del pannello.
    */
    public void cambiaScena(String nomeScena, String imgpath) {
        scenaCorrente =  nomeScena;
 
        imgCorrente = GestisciImmagine.load(imgpath);
        repaint();
    }
    
    /*
    Instrada il click alle interazioni specifiche della scena corrente.
    */
    private void interazioneClick(int x, int y) {

        switch (scenaCorrente) {

            case "BUNKER":
                interazioneBunkerClick(x, y);
                break;
            
            case "TV_ZOOM":
                interazioneTvZoomClick(x, y);
                break;
             
            case "CITY":
                interazioneCittaClick(x, y);
                break;

            case "CITY_CENTER":
                interazioneCentroCittaClick(x, y);
                break;

            case "METRO":
                interazioneMetroClick(x, y);
                break;

            case "BRIDGE":
                interazionePonteClick(x, y);
                break;

            case "NPC":
                interazioneNPCClick(x, y);
                break;

            case "TUNNEL":
                interazioneTunnelClick(x, y);
                break;

            case "LAB_CORRIDOR":
                interazioneCorridoioLabClick(x, y);
                break;
            
            case "CHEMICAL_LAB":
                interazioneLabChimicoClick(x, y);
                break;

            case "SECURITY_ROOM":
                interazioneStanzaSicurezzaClick(x, y);
                break;

            case "DIRECTOR_OFFICE":
                interazioneUfficioDirettoreClick(x, y);
                break;

            case "FINAL_ROOM":
                interazioneStanzaFinaleClick(x, y);
                break;
                
            case "FINALE_CORRETTO":
                // nessuna interazione possibile, il gioco è finito
            break;
 
            default:
                System.out.println("Scena non riconosciuta.");
        }
    }
    
    /*
    Interazioni nella stanza iniziale del bunker.
    */
    private void interazioneBunkerClick(int x, int y) {

        // Click sulla TV
        if (x > 938 && x < 1205 && y > 111 && y < 271) {
            usoTV = true;
            cambiaScena("TV_ZOOM", "/assets/bunker/tv_zoom.png");
        }

        // Click sulla cassa: la apre e consegna cibo e pistola al giocatore
        else if (x > 1019 && x < 1251 && y > 474 && y < 597) {
            if (!aperturaCassa1) {
                cambiaScena("BUNKER", "/assets/bunker/locker_open.png");
                
                Cibo cibo = new Cibo();
                cibo.usa(player);
                
                Arma pistola = new Arma("Pistola arrugginita", 15);
                player.addItem(pistola);
                pistola.usa(player); 
                
                player.addesperienza(25);

                aperturaCassa1 = true;
                interfacciaPanel.updateInterfaccia();
                
                // Torna indietro
                cambiaScena("BUNKER", "/assets/bunker/bunker_room.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("CASSA_VUOTA"));
            }
        }

        // Click sulla porta: permette l'uscita solo se TV e cassa sono stati interagiti
        else if (x > 545 && x < 709 && y > 189 && y < 408) {
            if(!usoTV || !aperturaCassa1){
                CustomDialog.mostra(frameParent, DialoghiManager.get("BUNKER_DOOR_BLOCCATA"));
            } else {
                cambiaScena("CITY", "/assets/city/city_destroyed.png");
            }
        }
    }    
    
    /*
    Zoom sulla TV: qualsiasi click riporta alla stanza del bunker.
    */    
    private void interazioneTvZoomClick(int x, int y) {
        if (x > 143 && x < 1148 && y > 1 && y < 607)
            cambiaScena("BUNKER", "/assets/bunker/bunker_room.png");
    }
    
    /*
    Interazioni nella città distrutta
    */
    private void interazioneCittaClick(int x, int y) {

        // Avanti verso centro città
        if (x > 600 && x < 723 && y > 298 && y < 446) {
            cambiaScena("CITY_CENTER", "/assets/city/city_center.png");
        } else if (x > 544 && x < 773 && y > 534 && y < 596) {
            // Torna indietro al bunker
            cambiaScena("BUNKER", "/assets/bunker/bunker_room.png");
        }
    }
    
    /*
    Interazioni nel centro città: 4 percorsi disponibili 
    (metro, ponte, strada) più indietro.
    */
    private void interazioneCentroCittaClick(int x, int y) {

        // Sinistra: metropolitana
        if (x > 872 && x < 999 && y > 318 && y < 332) {
            cambiaScena("METRO", "/assets/metro/metro_entrance.png");
        }

        // Destra: ponte autostradale
        else if (x > 871 && x < 1002 && y > 256 && y < 263) {
            cambiaScena("BRIDGE", "/assets/bridge/bridge_entrance.png");
        }

        // Centro: strada verso NPC / tunnel
        else if (x > 872 && x < 1000 && y > 289 && y < 300) {
            cambiaScena("NPC", "/assets/city/dying_soldier.png");
        }
        
        // Indietro
        else if (x > 870 && x < 1003 && y > 353 && y < 376) {
            cambiaScena("CITY", "/assets/city/city_destroyed.png");
        }
    }
    
    /*
    Interazioni nella metropolitana.
    */
    private void interazioneMetroClick(int x, int y) {

        /* Click quadro elettrico: avvia la sequenza di combattimento con i cani 
        e fa trovare il fusibile
        */
        if (x > 975 && x < 1036 && y > 284 && y < 389) {
            if (!fusibileTrovato) {
                cambiaScena("METRO", "/assets/metro/metro_quadroEle.png");
            
                CustomDialog.mostra(frameParent, DialoghiManager.get("METRO_SPENTA"));
                
                cambiaScena("METRO", "/assets/metro/metro_cani_infetti.png");
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("METRO_CANI"));

                boolean won = inizioCombat(
                    "Branco di cani infetti",
                    60,
                    15,
                    DialoghiManager.get("CANI_MORTE")
                );

                if (won) {
                    fusibileTrovato = true;
                    CustomDialog.mostra(frameParent, DialoghiManager.get("METRO_VITTORIA"));
                    cambiaScena("METRO", "/assets/metro/metro_entrance.png");
                }
                
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("FUSIBILE_TROVATO"));
            }
        }

        // Click per salire sulla metro: funziona solo con il fusibile, ma porta comunque alla morte
        else if (x > 375 && x < 574 && y > 256 && y < 398) {
            if (fusibileTrovato) {
                Fusibile fusibile = new Fusibile();
                fusibile.usa(player);
                cambiaScena("METRO", "/assets/metro/metro_morte.png");
                morte(DialoghiManager.get("METRO_MORTE"));
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("METRO_BLOCCATA"));
            }
        }
    }
    
    /*
    Interazioni sul ponte: avanzare scatena lo scontro con i droni.
    Vincere il combattimento non salva il giocatore: il ponte porta comunque alla morte.
    */
    private void interazionePonteClick(int x, int y) {
        
        // Click avanti
        if (x > 573 && x < 665 && y > 297 && y < 437) {
            cambiaScena("BRIDGE", "/assets/bridge/droni.png");
            
            CustomDialog.mostra(frameParent, DialoghiManager.get("DRONI"));

            boolean won = inizioCombat(
                "Squadrone di droni",
                60,
                20,
                DialoghiManager.get("DRONI_MORTE")
            );

            if (won) {
                morte(DialoghiManager.get("PONTE_MORTE"));
            }
        }
    }
    
    /*
    Interazioni durante l'esplorazione città.
    Il soldato va consultato prima di poter usare il tombino per accedere al tunnel.
    */
    private void interazioneNPCClick(int x, int y) {

        // Click sul soldato
        if (x > 187 && x < 266 && y > 428 && y < 539) {
            soldatoConsultato = true; 
            CustomDialog.mostra(frameParent, DialoghiManager.get("SOLDATO"));
        }

        // Click tombino verso tunnel
        else if (x > 731 && x < 797 && y > 448 && y < 457) {
            if (soldatoConsultato) {
                cambiaScena("TUNNEL", "/assets/tunnel/tunnel_entrance.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("TOMBINO_BLOCCATO"));
            }
        }
    }
    
    /*
    Interazioni nel tunnel sotterraneo.
    La cassa militare contiene medikit e blaster. In fondo al tunnel c'è
    la porta del laboratorio.
    */
    private void interazioneTunnelClick(int x, int y) {

        // Click cassa: aperta una sola volta
        if (x > 883 && x < 1055 && y > 438 && y < 530) {
            if (!aperturaCassa2) {   
                cambiaScena("TUNNEL", "/assets/tunnel/military_case_open.png");
                
                player.addItem(new Medikit());
                Arma blaster = new Arma("Blaster", 30);
                player.addItem(blaster);
                blaster.usa(player);
                player.addesperienza(50);
                
                interfacciaPanel.updateInterfaccia();
               
                aperturaCassa2 = true;   //blocca riaperture successive
                
                cambiaScena("TUNNEL", "/assets/tunnel/tunnel_entrance.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("CASSA_VUOTA"));
            }           
        }
        
        // Click fine tunnel
        else if (x > 358 && x < 444 && y > 234 && y < 311) {
            cambiaScena("LAB_CORRIDOR", "/assets/lab/lab_corridor.png");
            CustomDialog.mostra(frameParent, DialoghiManager.get("CHECKPOINT"));
        }
    }

    /*
    Interazioni nel corridoio del laboratorio.
    Da qui si interagisce con le altre stanze e al finale.
    */
    private void interazioneCorridoioLabClick(int x, int y) {
        
        // Click prima porta sinistra: laboratorio chimico
        if (x > 135 && x < 254 && y > 119 && y < 555) {
            cambiaScena("CHEMICAL_LAB", "/assets/lab/chemical_lab.png");
        }

        /*
        Click seconda porta sinistra: stanza sicurezza 
         accessibile solo dopo aver usato il PC nel laboratorio chimico
        */
        else if (x > 400 && x < 449 && y > 211 && y < 464) {
            if(!pcUsato) {
                CustomDialog.mostra(frameParent, DialoghiManager.get("PC_NON_USATO"));
            } else {
                cambiaScena("SECURITY_ROOM", "/assets/lab/security_room.png");
            }
        }

        // Click porta destra: ufficio del direttore, richiede il codice PIN
        else if (x > 1008 && x < 1121 && y > 171 && y < 574) {
            String inserito = JOptionPane.showInputDialog(this,
                DialoghiManager.get("INS_CODICE"));
                
            if (inserito != null && inserito.equals(code)){
                cambiaScena("DIRECTOR_OFFICE", "/assets/lab/director_office.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("CODICE_ERRATO"));
            }
        }
        
        // Click Exit centrale: richiede l'antidoto
        else if (x > 615 && x < 686 && y > 280 && y < 368) {
            if(player.hasItem("Antidoto")) {
                cambiaScena("FINAL_ROOM", "/assets/lab/final_boss.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("USCITA_FINALE_BLOCCATA"));
            }
        }
    }
    
    /*
    Interazioni nel laboratorio chimico.
    */
    private void interazioneLabChimicoClick(int x, int y) {
        
        //Click disco rigido
        if (x > 1034 && x < 1095 && y > 460 && y < 509) {
            if (!discoTrovato){
                discoTrovato = true;
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("HARDISK"));
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("HARDISK_TROVATO"));
            }
        }
        
        // Click PC: usabile solo dopo aver trovato il disco rigido
        else if (x > 195 && x < 311 && y > 271 && y < 326) {
            if (!discoTrovato) {
                CustomDialog.mostra(frameParent, DialoghiManager.get("HARDISK_NON_TROVATO"));
            } else if (!pcUsato) {
                pcUsato = true;
                
                // Avvia timer: se scade, il gas uccide il giocatore (checkpoint)
                labTimer = new LabTimer(10, () -> {
                    morte_checkpoint(DialoghiManager.get("GAS_MORTE"));
                });
                
                cambiaScena("CHEMICAL_LAB", "/assets/lab/zoom_pc.png");
 
                DiscoRigido hardisk = new DiscoRigido();
                hardisk.usa(player);
                
                labTimer.start();
                labTimerStarted = true;
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("FILE_RECUPERATO"));
                
                if (!labTimerStarted) return;
                
                cambiaScena("CHEMICAL_LAB", "/assets/lab/chemical_lab.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("PC_USATO"));
            }
        }
           
        //Click porta uscita: ferma il timer
        else if (x > 971 && x < 1054 && y > 152 && y < 317) {
            if (labTimerStarted && labTimer != null) {
                labTimer.stop();
                labTimerStarted = false;
                
                // il gas toglie vita al player
                player.subisciDanno(40);
                interfacciaPanel.updateInterfaccia();
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("LAB_USCITA"));
                
                // suggerisce di usare il medikit se ce l'ha
                if (player.hasItem("Medikit")) {
                    int scelta = JOptionPane.showConfirmDialog(this,
                        "Sei ferito. Vuoi usare il medikit?",
                        "Medikit",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (scelta == JOptionPane.YES_OPTION) {
                        // recupera il medikit dall'inventario e lo usa
                        for (Item item : player.getInventarioLista()) {
                            if (item instanceof Medikit) {
                                item.usa(player);
                                player.removeItem(item);
                                interfacciaPanel.updateInterfaccia();
                                break;  //necessario per evitare ConcurrentModificationException
                            }
                        }
                    }
                }
            }
            
            cambiaScena("LAB_CORRIDOR", "/assets/lab/lab_corridor.png");
        }
    }
    
    /*
    Interazioni nella stanza di sicurezza.
    */
    private void interazioneStanzaSicurezzaClick(int x, int y) {
        //Click terminale
        if (x > 271 && x < 499 && y > 222 && y < 357) {
            
            /*
            Genera il codice tramite REST al primo accesso, 
            e rimane mostrato tale ad ogni click
            */
            if (!codiceOttenuto) {
                code = RestCodeClient.getCodSicurezza();
                codiceOttenuto = true;
            
                CustomDialog.mostra(frameParent, "Terminale di sicurezza attivato.\n"
                    + "Codice generato: " + code + "\n\n"
                    + "Questo codice apre l'ufficio del direttore.");
            } else {
                CustomDialog.mostra(frameParent, "Terminale di sicurezza attivato.\n"
                    + "Codice generato: " + code + "\n\n"
                    + "Questo codice apre l'ufficio del direttore.");
            } 
        }
  
        //Click porta uscita
        else if (x > 861 && x < 945 && y > 184 && y < 366) {
            cambiaScena("LAB_CORRIDOR", "/assets/lab/lab_corridor.png");
        }
    }

    /*
    Interazioni nell'ufficio del direttore.
    Nella scrivania si trova la chiave magnetica; con essa si apre la cassaforte
    che contiene l'antidoto (da aggiungere all'inventario) necessario per accedere al finale.
    */
    private void interazioneUfficioDirettoreClick(int x, int y) {
        //Click scrivania
        if (x > 439 && x < 836 && y > 351 && y < 480) {
            if (!chiaveTrovata) {
                chiaveTrovata = true;   //blocca duplicati
                
                cambiaScena("DIRECTOR_OFFICE", "/assets/lab/desk_office.png");
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("CHIAVE"));
                
                cambiaScena("DIRECTOR_OFFICE", "/assets/lab/director_office.png");
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("CHIAVE_PRESA"));
            }
        }

        //Click cassaforte
        else if (x > 965 && x < 1076 && y > 268 && y < 419) {
            if (chiaveTrovata && !player.hasItem("Antidoto")) {
                cambiaScena("DIRECTOR_OFFICE", "/assets/lab/cassaforte_zoom.png");
                
                ChiaveMagnetica chiave = new ChiaveMagnetica();
                chiave.usa(player);
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("CASSAFORTE_APERTA"));

                player.addItem(new items.Antidoto());
                interfacciaPanel.updateInterfaccia();
                
                cambiaScena("DIRECTOR_OFFICE", "/assets/lab/director_office.png");
            } else if (player.hasItem("Antidoto")){
                CustomDialog.mostra(frameParent, DialoghiManager.get("ANTIDOTO_PRESO"));
            } else {
                CustomDialog.mostra(frameParent, DialoghiManager.get("CASSAFORTE_BLOCCATA"));
            }
        }

        //Click porta uscita
        else if (x > 227 && x < 341 && y > 144 && y < 350) {
            cambiaScena("LAB_CORRIDOR", "/assets/lab/lab_corridor.png");
        }
    }
    
    /*
    Interazioni parte finale.
    Il giocatore deve scegliere come usare l'antidoto:
    su sé stesso (scelta sbagliata, porta alla morte) o sul mostro (finale giusto).
    */
    private void interazioneStanzaFinaleClick(int x, int y) {
        if (finaleCompletato) return;   //blocca interazioni successive
        
        //Click sul mostro 
        if (x > 567 && x < 767 && y > 215 && y < 489) {
            Object[] options = {
                "Uso l'antidoto su me stesso",
                "Uso l'antidoto sul mostro"
            };

            int choice = JOptionPane.showOptionDialog(
                this,
                DialoghiManager.get("SCELTA_FINALE"),
                "Scelta finale",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
            
            //Scelta sbagliata
            if (choice == 0) {
                finaleCompletato = true;   //registra prima di morte()
                
                if (player.hasItem("Blaster")) {
                    //Ha il blaster: può combattere il mostro, ma perde comunque
                    
                    CustomDialog.mostra(frameParent, DialoghiManager.get("FINALE_SBAGLIATO1"));
                    
                    boolean won = inizioCombat(
                        "Mostro contaminato",
                        120,
                        25,
                        "Il mostro ti ha sopraffatto.\n\nSEI MORTO."
                    );        
                    
                    if (won) {
                        CustomDialog.mostra(frameParent, DialoghiManager.get("FINALE_SBAGLIATO2"));
                        
                        DatabaseManager.saveEnding(player.getnome(), "Finale sbagliato");
                        
                        ReportManager.salvaReport(player.getnome(), "Finale sbagliato", player.getContatoreMorti());
                        
                        SocketClient.inviaRisultato(player.getnome(), "Finale sbagliato", player.getContatoreMorti());
                        
                        morte("SEI MORTO.");
                    }
                //Non ha il Blaster, non può combattere
                } else {
                    CustomDialog.mostra(frameParent, DialoghiManager.get("FINALE_SBAGLIATO3"));
                    
                    DatabaseManager.saveEnding(player.getnome(), "Finale sbagliato");
                    
                    ReportManager.salvaReport(player.getnome(), "Finale sbagliato", player.getContatoreMorti());
                    
                    SocketClient.inviaRisultato(player.getnome(), "Finale sbagliato", player.getContatoreMorti());
                    
                    morte("SEI MORTO.");
                }
            }
            
            //Scelta giusta
            if (choice == 1) {
                finaleCompletato = true;   //registra la scelta
                
                DatabaseManager.saveEnding(player.getnome(), "Finale giusto");
                
                ReportManager.salvaReport(player.getnome(), "Finale giusto", player.getContatoreMorti());
                
                SocketClient.inviaRisultato(player.getnome(), "Finale giusto", player.getContatoreMorti());
                
                CustomDialog.mostra(frameParent, DialoghiManager.get("FINALE_GIUSTO"));
                
                cambiaScena("FINALE_CORRETTO", "/assets/end.png");
            }
        }
    }
    
    /*
    Gestisce la morte completa del giocatore: incrementa il contatore,
    resetta tutto lo stato e riporta al bunker.
    */
    private void morte(String message) {
        player.incrementaMorti();
        
        player.reset();   //reset completo inventario e stats
        
        if (labTimer != null) labTimer.stop();
        labTimerStarted = false;
        
        DatabaseManager.saveDeath(player.getnome(), message);
        
        CustomDialog.mostra(frameParent, message);
        
        //Reset di tutti i flag: il gioco riparte dall'inizio
        aperturaCassa1 = false;
        usoTV = false;
        fusibileTrovato = false;
        soldatoConsultato = false;
        aperturaCassa2 = false;
        chiaveTrovata = false;
        finaleCompletato = false;
        codiceOttenuto = false;
        discoTrovato = false;
        pcUsato = false;
        code = null;
        
        scenaCorrente = "BUNKER";
        imgCorrente = GestisciImmagine.load("/assets/bunker/bunker_room.png");
        interfacciaPanel.updateInterfaccia();
        
        repaint();
    }
    
    /*
    Gestisce la morte con checkpoint nel laboratorio: incrementa il contatore
    ma resetta solo i flag del laboratorio, riportando al corridoio senza perdere
    gli oggetti e i progressi nelle zone precedenti.
    */
    private void morte_checkpoint(String message) {
        player.incrementaMorti();

        if (labTimer != null) labTimer.stop();
        labTimerStarted = false;

        DatabaseManager.saveDeath(player.getnome(), message);
        CustomDialog.mostra(frameParent, message);

        //reset di alcuni flag
        chiaveTrovata = false;
        codiceOttenuto = false;
        finaleCompletato = false;
        discoTrovato = false;
        pcUsato = false;
        code = null;
        
        cambiaScena("LAB_CORRIDOR", "/assets/lab/lab_corridor.png");
        interfacciaPanel.updateInterfaccia();
    }
    
    /*
    Metodo di supporto che crea un Nemico e avvia il combattimento.
    In caso di sconfitta chiama direttamente morte() con il messaggio appropriato.
    Restituisce true se il giocatore ha vinto, false se ha perso
    */
    private boolean inizioCombat(String nomeNemico, int saluteNemico, int dannoNemico, String messaggioMorte) {

        Nemico nemico = new Nemico(nomeNemico, saluteNemico, dannoNemico);

        boolean vittoria = Combattimento.inizioFight(player, nemico);

        interfacciaPanel.updateInterfaccia();

        if (!vittoria) {
            morte(messaggioMorte);
            return false;
        }

        return true;
    }
}