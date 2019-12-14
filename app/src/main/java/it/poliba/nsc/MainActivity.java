package it.poliba.nsc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import it.poliba.nsc.dboperation.VerificaAccoutBackground;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button buttonLogin;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.btn_login);
//gestisco azioni da compiere al momento del click sul pulsante Login (verifica presenza dipendente nel DB)
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.campi_vuoti), Toast.LENGTH_SHORT).show();
                } else {

                    VerificaAccoutBackground verificaBackgroud = new VerificaAccoutBackground();

                    try {
                        ArrayList<String> dipendenti = verificaBackgroud.execute(email,password).get(3000, TimeUnit.MILLISECONDS);

                        if(dipendenti.get(0).isEmpty()) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connessione_assente), Toast.LENGTH_SHORT).show();
                        } else if (dipendenti.get(0).equals("-1")){
                            Toast.makeText(getApplicationContext(), "Campi ERRATI", Toast.LENGTH_SHORT).show();
                        } else {
                            Dipendente dipLogin = decodeJsonDipendente(dipendenti.get(0));

                            Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
                            myIntent.putExtra("idDipendente",dipLogin.getIdDipendente());
                            myIntent.putExtra("nomeDipendente",dipLogin.getNome());
                            myIntent.putExtra("emailDipendente",dipLogin.getEmail());
                            startActivity(myIntent);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
//da qui in poi si gestisce l'NFC
    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = createNdefMessage("Il contenuto della mia stringa!");

            writeNdefMessage(tag, ndefMessage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag writen!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }

    }
//metodo che permette la comunicazione del tag NFC (in scrittura in tal caso)
    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // format tag with the ndef format and writes the message.
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag writen!", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.e("writeNdefMessage", e.getMessage());
        }

    }


    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }


    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }
//metodo per conversione stringa, ottenuta tramite file php (VerificaLogin.php), in formato json 
    private Dipendente decodeJsonDipendente(String dipendenteJson) {
        Dipendente dipendente = new Dipendente();

        try {
            JSONObject jsonObj = new JSONObject(dipendenteJson);

            dipendente.setIdDipendente(jsonObj.getString("ID_Dipendente"));
            dipendente.setNome(jsonObj.getString("Nome"));
            dipendente.setCognome(jsonObj.getString("Cognome"));
            dipendente.setEmail(jsonObj.getString("Email"));
            dipendente.setPassword(jsonObj.getString("Password"));
            dipendente.setCodUnivoco(jsonObj.getString("Codice_univoco"));

            return dipendente;

        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            return null;
        }
    }
//metodo per acquisizione indirizzo MAC del dispositivo corrente
    public static String getMacAddress(String interfaceName) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (TextUtils.equals(networkInterface.getName(), interfaceName)) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    StringBuilder builder = new StringBuilder();
                    for (byte b : bytes) {
                        builder.append(String.format("%02X:", b));
                    }

                    if (builder.length() > 0) {
                        builder.deleteCharAt(builder.length() - 1);
                    }

                    return builder.toString();
                }
            }

            return getMacAddress("wlan0");

        } catch (SocketException e) {
            return "ERRORE";
        }
    }

}
