package it.poliba.nsc.dboperation;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilizzata per effettuare la connessione al database.
 */

public class ConnectionDB {

    public static final String CODIFICA = "UTF-8";
    private static String localhost = "192.168.1.141";

    private URL url;
    private HttpURLConnection http;

    public ConnectionDB(String connstr, String requestMethod) throws IOException {
        url = new URL(connstr);
        http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(requestMethod);
        http.setDoInput(true);
        http.setDoOutput(true);

    }

    public static String getLocalhost() {
        return localhost;
    }

    public static void setLocalhost(String localhost) {
        ConnectionDB.localhost = localhost;
    }

    /**
     * Effettua una connessione HTTP col server
     * Invia una query o dei dati tramite metodo POST al server
     * Riceve dal server il risultato delle query e lo ritorna
     *
     * @param data
     * @return Lista di stringhe in formato JSON
     */

    public List<String> streamConnection(String data) {

        List<String> risposta = new ArrayList<>();

        try {
            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));

            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));

            String line;

            while ((line = reader.readLine()) != null) {
                risposta.add(line);
            }

            reader.close();
            ips.close();
            http.disconnect();
        } catch (IOException e) {
            Log.e("IOException", "" + e);
        }

        if (risposta.isEmpty()) {
            risposta.add("");
        }

        return risposta;
    }


}
