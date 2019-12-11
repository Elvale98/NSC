package it.poliba.nsc.dboperation;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class VerificaAccoutBackground extends AsyncTask<String, Void, ArrayList<String>> {

    //Viene richiamato dal metodo execute()
    @Override
    protected ArrayList<String> doInBackground(String... voids) {
        String email = voids[0];
        String password = voids[1];

        ArrayList<String> result = new ArrayList<>();

        String connstr = "http://" + ConnectionDB.getLocalhost() + "/PHP/verificaLogin.php";

        try {

            String data = URLEncoder.encode("email", ConnectionDB.CODIFICA) + "=" + URLEncoder.encode(email, ConnectionDB.CODIFICA)
                    + "&&" + URLEncoder.encode("password", ConnectionDB.CODIFICA) + "=" + URLEncoder.encode(password, ConnectionDB.CODIFICA);
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