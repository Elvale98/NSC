package it.poliba.nsc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.MacAddress;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HomeActivity extends AppCompatActivity {

    private TextView textNome;
    private TextView textEmail;
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
                setContentView(R.layout.activity_nfc);

                WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String address = info.getMacAddress();

                Toast.makeText(getApplicationContext(), "MAC Address: " + getMacAddress("wlan0"), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
