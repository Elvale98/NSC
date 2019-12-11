package it.poliba.nsc.dboperation;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class VerificaMAC extends AsyncTask<String, Void, ArrayList<String>> {

    //Viene richiamato dal metodo execute()
    @Override
    protected ArrayList<String> doInBackground(String... voids) {
        String codice_univoco = voids[0];

        ArrayList<String> result = new ArrayList<>();

        String connstr = "http://" + ConnectionDB.getLocalhost() + "/PHP/verificaMAC.php";

        try {

            String data = URLEncoder.encode("codice_univoco", ConnectionDB.CODIFICA) + "=" + URLEncoder.encode(codice_univoco, ConnectionDB.CODIFICA);
            /*
             * Effettua la connessione e ritorna il risultato.
             * */
            ConnectionDB conn = new ConnectionDB(connstr, "POST");
            result.addAll(conn.streamConnection(data));

        } catch (IOException e) {
            Log.e("IOException", "" + e);
        }

        return result;
    }
}