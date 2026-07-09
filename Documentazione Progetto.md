# A-P-N-E-A

## Autori

DeepCoders: 

   Matteo Notarachille
  
   Valerio Stasi
  
   Marco Michele Pio Scavo

## Indice

- [Descrizione dell’avventura](#Descrizione-dell’avventura)
- [Modalità d’avvio](#Modalità-d’avvio)
- [Soluzione del gioco](#Soluzione-del-gioco)
- [Progettazione](#Progettazione)
- [Diagramma delle classi](#Diagramma-delle-classi)
- [Specifica algebrica](#Specifica-algebrica)
- [Dettagli implementativi](#Dettagli-implementativi)

# <a name="_heading=h.f3pm5wycbytn"></a>Descrizione dell’avventura
A-P-N-E-A è un'avventura grafica point-and-click sviluppata in Java con interfaccia Swing.

***TRAMA:*** 

*“Non ricordi il tuo nome. Non ricordi chi eri.*

\
*Ti risvegli in un bunker sotterraneo post-apocalittico senza memoria. Davanti a te una vecchia televisione ancora accesa ti guida nei primi istanti di sopravvivenza, insegnandoti a utilizzare gli oggetti, gestire l'inventario, recuperare risorse. Ogni oggetto può fare la differenza tra la vita e la morte.\
Quando il portellone del bunker si apre, scopri una città completamente devastata: edifici in rovina, fumo e macerie ovunque. Da questo momento il gioco cambia completamente tono: il tutorial lascia spazio alla vera sopravvivenza.\
Non sai di preciso dove andare, conosci soltanto la meta da raggiungere, ovvero il laboratorio segreto, il quale, però, non è raggiungibile fin da subito. Esiste una sola strada percorribile che conduce fino al centro della città, un tempo piazza principale e oggi centro della desolazione.\
Lì un vecchio cartello ti indica tre direzioni differenti, oltre alla possibilità di tornare indietro. Ogni percorso presenta ambientazioni uniche, pericoli e decisioni fondamentali che influenzeranno il destino del protagonista.\
La prima direzione indica una metropolitana: atmosfera buia e claustrofobica. \
La seconda porta ad un ponte autostradale sospeso, popolato da misteriose presenze. \
La terza, invece, permette di continuare l’esplorazione tra le rovine della città, magari necessaria ad ottenere informazioni utili a comprendere ciò che è accaduto.* 

*Il protagonista, quindi deve scoprire dove ogni percorso conduca per arrivare al finale sano e salvo.”*

Durante l'esplorazione il giocatore può raccogliere oggetti, come cibo, armi, utilities…,  combattere nemici, guadagnare esperienza e salire di livello. Il sistema di morte non è definitivo: ad ogni morte il giocatore ricomincia dall'inizio, tranne se arriva ad un checkpoint  (gestita in modo tale da farlo ripartire da quel punto in poi). Ogni morte e ogni finale vengono registrati su database e, al termine, i risultati vengono trasmessi a un server centrale che aggiorna una classifica ordinata per numero di morti.

## <a name="_heading=h.a8o3w5xh2qux"></a>Modalità d’avvio
Dopo l’esecuzione del gioco, attraverso un pop up, viene offerta una possibilità di scelta di avvio, in due modalità: server o client. Se si vuole testare l’opzione classifica locale multigiocatore, bisogna scegliere entrambe le modalità avviando due esecuzioni del gioco, dove nella prima scegliere la voce “Avvia come server” e nella seconda scegliere la voce “Connettiti come client” e lasciare nel campo “Inserisci l’IP del computer server” il localhost che è già scritto come valore di default (valore che permette di connettersi in automatico al server già avviato sulla stessa macchina).

La finestra server può essere chiusa solo dopo che è terminata la sessione di gioco come client. Si possono giocare entrambe le esecuzioni se si vogliono vedere visualizzati due risultati in classifica.
## Soluzione del gioco

L'obiettivo è procurarsi un antidoto contenuto nella cassaforte dell'ufficio del direttore del laboratorio e portarlo fino all’uscita di esso, dove una scelta determina il destino del giocatore: usare l'antidoto su sé stessi (scelta sbagliata, porta alla morte) oppure somministrarlo al mostro contaminato (scelta giusta, unico finale di sopravvivenza).



**Schema soluzione:**

*Bunker -> Centro città -> Esplorazione città –> Tunnel militare -> Laboratorio ->* 

*Lab chimico ->  Stanza di sicurezza -> Ufficio del direttore -> Uscita -> Boss finale*

# <a name="_heading=h.w3rvyyps1i5n"></a>Progettazione
**Individuazione delle classi**

Le classi sono state individuate seguendo il paradigma object-oriented, partendo dalle entità del dominio del gioco: il giocatore, i nemici, gli oggetti dell'inventario, le schermate di gioco e i servizi di supporto.

Ogni entità ha responsabilità ben definite e circoscritte, rispettando il principio di singola responsabilità (SRP). Ad esempio, Player gestisce esclusivamente lo stato del giocatore (salute, esperienza, danno, inventario), Combattimento incapsula tutta la logica di combattimento, DatabaseManager si occupa solo della persistenza su SQLite, e ScenePanel organizza le interazioni tra le scene grafiche.

**Competenze delle classi principali**

- **Player** — mantiene lo stato del giocatore: nome, salute, salute massima, esperienza, danno, level-up bonus, contatore morti e inventario (ArrayList<Item>). Espone metodi per curare, subire danno, aggiungere/rimuovere/verificare oggetti, accumulare esperienza (con level-up automatico a 75 punti), resettare lo stato completo (morte normale) e incrementare il contatore morti.
- **Item (abstract) + sottoclassi** — gerarchia di oggetti dell'inventario. La classe astratta definisce il contratto usa(Player). Le sottoclassi concrete (Cibo, Arma, Medikit, Fusibile, DiscoRigido, ChiaveMagnetica, Antidoto) implementano l'effetto specifico di ogni oggetto sul giocatore.
- **Nemico** — rappresenta un avversario con nome, salute e danno. Espone metodi per subire danno, verificare se è vivo, leggere il danno e il nome.
- **Combattimento** — classe di utilità (metodo statico) che implementa il loop di combattimento usando JOptionPane. Gestisce i rifiuti consecutivi dell'utente (dopo 2 rifiuti il nemico attacca comunque) e restituisce true (vittoria) o false (sconfitta).
- **ScenePanel** — cuore del gioco, estende JPanel. Gestisce la scena corrente, l'immagine di sfondo, tutti i flag di stato (aperture casse, oggetti trovati, ecc.) e il routing dei click alle funzioni di interazione di ogni scena. Contiene anche la logica di morte normale e morte checkpoint.
- **InterfacciaPanel** — estende JPanel; interfaccia del gioco, mostra barra HP, barra EXP e inventario testuale. Si aggiorna tramite updateInterfaccia().
- **FinestraGioco** — estende JFrame. Mostra la schermata titolo, raccoglie il nome del giocatore e poi istanzia ScenePanel.
- **DatabaseManager** — gestisce la connessione JDBC a SQLite (file A-P-N-E-A.db), crea le tabelle deaths e endings, e offre metodi per salvare morti e finali.
- **SocketServer / SocketClient** — gestiscono la comunicazione di rete. Il server si avvia in un thread separato, accetta connessioni in un loop, riceve i risultati dei client e aggiorna la classifica su file. Il client invia il risultato al termine della partita.
- **RestCodeClient** — effettua una chiamata REST GET a random.org per ottenere un codice di sicurezza casuale a 4 cifre usato per aprire l'ufficio del direttore.
- **DialoghiManager** — carica da file (dialoghi.txt) una mappa chiave-valore dei testi di gioco, con supporto a \n inline.
- **ReportManager** — scrive un file di testo (report\_partita.txt) con il riepilogo della partita al termine.
- **LabTimer** — incapsula un javax.swing.Timer con countdown: avvia un conto alla rovescia di N secondi e, allo scadere, invoca un Runnable (la morte per gas nel laboratorio chimico).
- **GestisciImmagine** — classe di utilità per caricare immagini da risorse del classpath, con fallback su BufferedImage vuota in caso di errore.
- **CustomDialog** — estende JDialog, mostrando un Dialog personalizzato (finestra modale senza bordi, sfondo scuro, bordo rosso) usato in alternativa al JOptionPane standard per i dialoghi narrativi.
- **ConsoleUI** — estende JPanel, serve per la schermata titolo, mostra l'immagine di copertina e avanza al click del mouse.

**Organizzazione in package**

|**Package**|**Contenuto**|
| :- | :- |
|**api**|RestCodeClient |
|**combattimento**|Combattimento |
|**database**|DatabaseManager|
|**grafica**|CustomDialog, FinestraGioco, GestisciImmagine, InterfacciaPanel, ScenePanel — tutto il layer visuale|
|**items**|Item (abstract), Arma, Cibo, Medikit, Fusibile, DiscoRigido, ChiaveMagnetica, Antidoto|
|**nemico**|Nemico|
|**player**|Player|
|**play**|Play — main|
|**rete**|SocketServer, SocketClient|
|**ui**|ConsoleUI|
|**util**|DialoghiManager, LabTimer, ReportManager|

La separazione in package garantisce che ogni layer del sistema (grafica, logica di gioco, rete, persistenza, utilità) sia indipendente e modificabile senza impattare gli altri.
## Diagramma delle classi
**Link Mermaid diagramma delle classi  intero progetto:**

<https://mermaid.ai/d/2ae9f919-13a0-4165-b55e-eb097cf980a9>


**Commento**

Il principio di **ereditarietà** è centrale nella gerarchia Item: la classe astratta definisce la struttura comune (nome, metodo usa astratto) e ogni sottoclasse concreta specializza il comportamento. Questo è un esempio di **polimorfismo per inclusione**: ScenePanel può chiamare item.usa(player) su qualsiasi oggetto dell'inventario senza conoscerne il tipo concreto, e il meccanismo di dynamic dispatch selezionerà a runtime l'implementazione corretta.

La **composizione** è usata estensivamente: ScenePanel contiene (\*--) un InterfacciaPanel e un LabTimer, e Player aggrega (o--) una collezione di Item. Le classi di servizio (DatabaseManager, RestCodeClient, DialoghiManager, ReportManager) sono progettate con tutti metodi statici, fungendo da namespace di utilità senza stato proprio, un pattern comune in Java per i layer di accesso ai dati e le utility.
## Specifica algebrica
E’ stato scelto di fornire la specifica algebrica della struttura dati **Inventario**, ovvero la collezione di Item gestita all'interno della classe Player (concretamente realizzata tramite ArrayList<Item>).

1. **Specifica sintattica:**

   sorts: inventario, item, boolean, integer

   operations:

   `    `nuovoInventario() → inventario

   `    `aggiungi(inventario, item) → inventario

   `    `rimuovi(inventario, item) → inventario

   `    `contiene(inventario, item) → boolean

   `    `isEmpty(inventario) → boolean

   `    `dimensione(inventario) → integer

1. **Costruttori:** nuovoInventario(), aggiungi(inv, i)

**Osservazioni:**  isEmpty(inv'), dimensione(inv'), contiene(inv', j), rimuovi(inv', j)

||***nuovoInventario()***|***aggiungi(inv, i)***|
| :- | :- | :- |
|***isEmpty(inv')***|true|false|
|***dimensione(inv')***|0|dimensione(inv) + 1|
|***contiene(inv', j)***|false|if i = j then true else contiene(inv, j)|
|***rimuovi(inv', j)***|error|if i = j then inv else aggiungi(rimuovi(inv, j), i)|

1. **Specifica semantica:**

   declare inv: inventario, i, j: item

   isEmpty(nuovoInventario()) = true

   isEmpty(aggiungi(inv, i)) = false

   dimensione(nuovoInventario()) = 0

   dimensione(aggiungi(inv, i)) = dimensione(inv) + 1

   contiene(nuovoInventario(), i) = false

   contiene(aggiungi(inv, i), j) = if i = j then true

   `                                               `else contiene(inv, j)

   rimuovi(aggiungi(inv, i), j) = if i = j then inv

   `                                            `else aggiungi(rimuovi(inv, j), i)

1. **Specifica di restrizione:**

   rimuovi(nuovoInventario(), i) = error

   contiene(nuovoInventario(), i) = false
## <a name="_heading=h.5u5phhj9vnqv"></a>Dettagli implementativi
### <a name="_heading=h.nkmxinfn4tr8"></a>Programmazione generica
La programmazione generica in Java permette di definire classi, interfacce e metodi parametrizzati rispetto al tipo, garantendo type-safety a compile-time senza rinunciare al riuso del codice. Il meccanismo sottostante è la type erasure: i parametri di tipo vengono eliminati dal compilatore e sostituiti con il loro bound (Object se non specificato), per garantire la compatibilità con la JVM.

Nel progetto, la programmazione generica è utilizzata in modo diretto nella classe Player, dove l'inventario è dichiarato come ArrayList<Item>. Questo garantisce che solo oggetti di tipo Item (o sottoclassi) possano essere inseriti nella collezione, eliminando la necessità di cast espliciti e prevenendo ClassCastException a runtime. Analogamente, il metodo getInventarioLista() restituisce ArrayList<Item>, consentendo al chiamante (in ScenePanel) di iterare con type-safety:

for (Item item : player.getInventarioLista()) {

if (item instanceof Medikit) { ... }

}

Anche SocketServer usa List<String> per la raccolta dei risultati, e DialoghiManager usa HashMap<String, String> per la mappa chiave-valore dei dialoghi. In entrambi i casi, la genericità evita i cast di tipo.
### File
<a name="_heading=h.1kays2bx78hl"></a>Java mette a disposizione due API principali per la gestione dei file: la I/O classica (package java.io) basata su stream di byte e di caratteri, e la NIO.2 (package java.nio.file) che introduce Path, Files e operazioni atomiche. Nel progetto la gestione dei file viene implementata attraverso l'API classica, che Java mette a disposizione, I/O (package java.io), basata su stream di byte e di caratteri con reader/writer bufferizzati.

DialoghiManager legge il file dialoghi.txt usando BufferedReader su FileReader, che aggiunge un layer di buffering per ridurre il numero di operazioni di I/O effettive sul filesystem. Ogni riga viene analizzata cercando il separatore ‘=’ e spezzata con split("=", 2) per supportare valori che contengono ulteriori ‘=’.

ReportManager (per report\_partita.txt) e SocketServer (per classifica.txt) scrivono su file di testo usando BufferedWriter su FileWriter. In entrambi i casi il file viene aperto con la flag false (overwrite), cioè il contenuto precedente viene sovrascritto ad ogni aggiornamento. 

Tutti e tre i casi usano il costrutto try-with-resources per garantire la chiusura automatica dello stream anche in caso di eccezione, rispettando il principio di gestione sicura delle risorse:

try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeFile, false))) { bw.write(...); }
### Database (JDBC)
JDBC (Java Database Connectivity) è l'API standard di Java per l'accesso ai database relazionali. Si basa su un layer di astrazione che separa il codice applicativo dal driver specifico del DBMS tramite l'interfaccia Connection, Statement e PreparedStatement.

Nel progetto si usa SQLite tramite JDBC. DatabaseManager apre una connessione con DriverManager.getConnection("jdbc:sqlite:A-P-N-E-A.db") e crea due tabelle (deaths ed endings) con Statement.execute() usando SQL DDL con la clausola CREATE TABLE IF NOT EXISTS.

Per le operazioni di scrittura si usa PreparedStatement con parametri posizionali (?), prevenendo SQL injection e rendendo il codice più leggibile:

`	`PreparedStatement stmt = conn.prepareStatement( 

"INSERT INTO deaths(player\_name, cause) VALUES(?, ?)"); 

stmt.setString(1, playerName); 

stmt.setString(2, cause); 

stmt.executeUpdate();

I metodi saveDeath e saveEnding usano il try-with-resources (rilascio automatico).
### Lamba Expression (compresi stream e pipeline)
Le lambda expression permettono di trattare funzioni come valori di prima classe, abilitando uno stile di programmazione funzionale. Sono implementazioni anonime di **interfacce funzionali** (interfacce con un solo metodo astratto), come Runnable, Comparator, ActionListener e le interfacce del package java.util.function.

Nel progetto le lambda compaiono in diversi punti fondamentali:

- ***ActionListener su Timer (LabTimer):***

timer = new Timer(1000, e -> { 

this.secondi--; 

if (this.secondi < 0) { 

timer.stop(); 

onTimeExpired.run(); 

}

` `});

Il parametro onTimeExpired è un Runnable, passato come lambda dalla chiamata in ScenePanel, che invoca morte\_checkpoint(...). Questo è un esempio di **callback funzionale**: il timer non conosce cosa fare allo scadere, conosce solo come farlo fare.

- ***Runnable per avviare il server in background (SocketServer):***

new Thread(() -> { ... }).start();

`	`Il corpo del thread è una lambda che cattura serverSocket per closure.

- ***Stream API per costruire la stringa dell'inventario (Player):***
  - return inventario.stream() 

    .map(Item::getnome) 

    .collect(java.util.stream.Collectors.joining(" | "));

Questa è una **pipeline di stream** composta da: stream() (sorgente), map (trasformazione tramite method reference Item::getnome), collect. È un esempio tipico di programma funzionale su collezioni: dichiarativo, senza variabili mutabili locali.

- ***Comparator lambda in SocketServer:***
  - risultati.sort((a, b) -> { 

int mortiA = estraiMorti(a);

int mortiB = estraiMorti(b);

return Integer.compare(mortiA, mortiB); 

});

Il Comparator<String> è espresso come lambda che estrae il numero di morti da ogni stringa e li confronta, senza bisogno di una classe anonima esplicita.

- ***Runnable come callback per la schermata titolo:***
  - ConsoleUI.mostraTitolo(this, () -> avviaGioco());

Il Runnable () -> avviaGioco() viene eseguito al click sul pannello titolo.

### <a name="_heading=h.2qv9rfkka56v"></a>SWING
Swing è il framework GUI di Java, basato su una gerarchia di componenti (Component → Container → JComponent) con un modello ad eventi (pattern Observer): i componenti emettono eventi, i listener reagiscono. L'intero rendering di Swing avviene sul Event Dispatch Thread (EDT), il thread dedicato alla GUI.

Nel progetto l'architettura grafica è strutturata su JFrame/JPanel:

FinestraGioco estende JFrame ed è la finestra principale (1280×720, non ridimensionabile). Usa il pattern callback per passare il controllo da ConsoleUI a ScenePanel dopo aver ottenuto il nome del giocatore tramite JOptionPane.showInputDialog.

ScenePanel estende JPanel e ridefinisce paintComponent(Graphics g) per disegnare l'immagine di sfondo corrente. Aggiunge un MouseAdapter per intercettare i click del mouse e delegarli a interazioneClick(x, y), che usa uno switch sulla scena corrente per il routing. Il metodo cambiaScena aggiorna imgCorrente e chiama repaint() per forzare il ridisegno.

InterfacciaPanel estende JPanel con layout null (posizionamento assoluto). Contiene JProgressBar per HP ed EXP e un JLabel per l'inventario. Il metodo updateInterfaccia() aggiorna i valori e chiama repaint().

CustomDialog estende JDialog con setUndecorated(true) (senza bordi nativi), sfondo personalizzato (Color(20,20,20)), bordo rosso (BorderFactory.createLineBorder) e un pulsante "CONTINUA" con styling custom. Questo è un esempio di personalizzazione oltre i default di Swing.

Per le interazioni semplici si usa JOptionPane (dialoghi standard di conferma, input, messaggio), mentre per i dialoghi narrativi si usa CustomDialog per coerenza visiva con l'estetica del gioco.

L'invio della finestra all'EDT avviene correttamente nel main:

`	`javax.swing.SwingUtilities.invokeLater(() -> new FinestraGioco());

### Thread e programmazione concorrente
In Java ogni programma ha almeno il thread principale (main thread) e il thread dell'Event Dispatch Thread (EDT) per Swing. La programmazione concorrente consente l'esecuzione parallela di più flussi di controllo, con tutti i rischi di race condition e deadlock che ne derivano se gli accessi alle risorse condivise non sono sincronizzati.

Nel progetto i thread compaiono in tre contesti:

- ***Timer di Swing (LabTimer):*** javax.swing.Timer non crea un thread separato nel senso tradizionale — i suoi eventi vengono eseguiti sull'EDT. Questo è thread-safe rispetto all'interfaccia grafica: quando il countdown scade e viene invocato morte\_checkpoint, questo avviene già sull'EDT, quindi non c'è rischio di aggiornare la GUI da un thread sbagliato.
- ***Thread del server socket (SocketServer):*** il server viene avviato con new Thread(() -> { ... }).start(), creando un thread demone in background che esegue il loop accept(). Per ogni client connesso viene creato un ulteriore thread figlio con new Thread(() -> { gestisciClient(client); }).start(), consentendo la gestione concorrente di più client.
- ***Interazione tra EDT e thread di rete:*** quando un client invia il risultato via SocketClient.inviaRisultato(), questa chiamata avviene sull'EDT (è invocata da metodi di ScenePanel). Poiché si tratta di un'operazione di I/O bloccante (attende la connessione TCP), potrebbe in teoria bloccare la GUI per la durata della connessione. In questo progetto il rischio è limitato dalla natura locale/LAN dell'uso, ma in un sistema reale sarebbe opportuno eseguire l'invio in un thread separato.
### Socket e/o REST
**Socket TCP (classi usate: SocketServer, SocketClient):**\
Le socket TCP forniscono un canale di comunicazione bidirezionale e affidabile tra processi in rete. In Java si usano ServerSocket (lato server, si mette in ascolto) e Socket (lato client, si connette).

Nel progetto, all'avvio il giocatore sceglie se fare da server o da client. Il server avvia SocketServer.avvia(), che crea un ServerSocket sulla porta 12345 con setReuseAddress(true) (per evitare errori di "address already in use" tra sessioni ravvicinate) e resta in ascolto. Al termine della partita (qualunque finale), SocketClient.inviaRisultato() apre una Socket verso il server, invia una riga di testo con nome, finale e numero di morti, e la chiude. Il server riceve la riga, la aggiunge alla lista e aggiorna il file classifica.txt ordinato per numero di morti crescente.

Il risultato inviato viene visualizzato al serve con un semplice formato testuale a pipe: "Nome | Finale: X | Morti: N", analizzato lato server con split("Morti: ").

**API REST (classe usata: RestCodeClient):**\
REST (Representational State Transfer) è uno stile architetturale per la comunicazione tra sistemi via HTTP, usando i verbi standard (GET, POST, PUT, DELETE) su risorse identificate da URL. Una chiamata REST GET non ha effetti collaterali sul server.

RestCodeClient.getCodSicurezza() apre una connessione HTTP con HttpURLConnection, imposta il metodo GET e legge la risposta con BufferedReader. L'URL punta al servizio public random.org, che restituisce un intero casuale tra 1000 e 9999 in formato plain text. Il codice ottenuto viene usato come PIN per aprire l'ufficio del direttore, introducendo un elemento di casualità verificabile esternamente ad ogni partita.


