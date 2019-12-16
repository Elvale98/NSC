package it.poliba.nsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import it.poliba.nsc.dboperation.VerificaAccoutBackground;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.btn_login);

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
    }

    private Dipendente decodeJsonDipendente(String dipendenteJson) {
        Dipendente dipendente = new Dipendente();

        try {
            JSONObject jsonObj = new JSONObject(dipendenteJson);

            dipendente.setIdDipendente(jsonObj.getString("ID_Dipendente"));
            dipendente.setNome(jsonObj.getString("Nome"));
            dipendente.setCognome(jsonObj.getString("Cognome"));
            dipendente.setEmail(jsonObj.getString("Email"));
            dipendente.setPassword(jsonObj.getString("Password"));

            return dipendente;

        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            return null;
        }
    }
}
