package it.poliba.nsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private TextView textNome;
    private TextView textEmail;
    private TextView textMACAddress;
    private Button buttonLogout;
    private Button buttonTimbra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent myIntent = getIntent();
        String idDipendente = myIntent.getStringExtra("idDipendente");
        String nomeDipendente = myIntent.getStringExtra("nomeDipendente");
        String emailDipendente = myIntent.getStringExtra("emailDipendente");

        textNome = findViewById(R.id.nome);
        textEmail = findViewById(R.id.email);

        buttonLogout = findViewById(R.id.btn_logout);
        buttonTimbra = findViewById(R.id.btn_timbra);

        textNome.setText(nomeDipendente);
        textEmail.setText(emailDipendente);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        buttonTimbra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), NFC.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "IMPLEMENTARE NFC", Toast.LENGTH_LONG).show();

                textMACAddress= findViewById(R.id.MACAddress);
                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String address = info.getMacAddress();
                Toast.makeText(getApplicationContext(), "MAC Address: "+ MainActivity.getMacAddress("wlan0"), Toast.LENGTH_LONG).show();
            }
        });
    }
}