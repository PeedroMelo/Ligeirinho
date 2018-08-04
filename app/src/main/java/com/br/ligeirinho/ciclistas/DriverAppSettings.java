package com.br.ligeirinho.ciclistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.br.ligeirinho.R;
import com.google.firebase.auth.FirebaseAuth;

public class DriverAppSettings extends AppCompatActivity {

    LinearLayout dadosPessoais, termosCondicoes, faleConosco, trabalheConosco;
    Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_app_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Configurações");


        dadosPessoais   = (LinearLayout) findViewById(R.id.dadosPessoais);
        termosCondicoes = (LinearLayout) findViewById(R.id.termosCondicoes);
        faleConosco     = (LinearLayout) findViewById(R.id.faleConosco);
        trabalheConosco = (LinearLayout) findViewById(R.id.trabalheConosco);

        sair = (Button) findViewById(R.id.sair);


        dadosPessoais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverAppSettings.this, DriverSettings.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DriverAppSettings.this, DriverLogin.class );
                startActivity(intent);
                finish();
                return ;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, DriverMainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
