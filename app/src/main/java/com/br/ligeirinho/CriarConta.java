package com.br.ligeirinho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CriarConta extends AppCompatActivity {

    TextView tipoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        Intent params = new Intent();
        String tipo = params.getStringExtra("tipo");

        tipoLogin = (TextView) findViewById(R.id.tipoLogin);

        if(tipo == "cliente"){
            tipoLogin.setText("Cliente");
        }else {
            tipoLogin.setText("Ciclista");
        }

    }


}
