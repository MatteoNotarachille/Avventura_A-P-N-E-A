package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
Effettua una chiamata REST al servizio esterno random.org per ottenere
un codice numerico casuale a 4 cifre. 
In caso di errore di rete restituisce il codice di fallback "0000".

@author DeepCoders
*/

public class RestCodeClient {
    
    /*
    Contatta random.org via HTTP GET e legge il numero intero restituito.
    Restituisce una stringa con il codice a 4 cifre, oppure "0000" in caso di errore
    */
    public static String getCodSicurezza() {
        try {
            URL url = new URL("https://www.random.org/integers/?num=1&min=1000&max=9999&col=1&base=10&format=plain&rnd=new");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String code = reader.readLine();

            reader.close();
            conn.disconnect();

            return code;

        } catch (Exception e) {
            return "0000";
        }
    }
}
