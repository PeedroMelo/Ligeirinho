package com.br.ligeirinho;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.br.ligeirinho.clientes.CustomerLogin;
import com.br.ligeirinho.clientes.CustomerMainActivity;
import com.br.ligeirinho.ciclistas.DriverLogin;
import com.br.ligeirinho.ciclistas.DriverMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ChooseLogin extends AppCompatActivity {

    private Button mDriver;
    private Button mCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
        getSupportActionBar().hide();

        Display display = getWindowManager().getDefaultDisplay();
        double width = display.getWidth();

        double doubleSize = (width/5)*4.4;
        int editTextSize = (int) doubleSize;

        mDriver   = (Button) findViewById(R.id.driver_id);
        mCustomer = (Button) findViewById(R.id.customer_id);

        mDriver.setHeight(editTextSize);
        mCustomer.setHeight(editTextSize);

        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(ChooseLogin.this, DriverLogin.class);
            startActivity(intent);
            finish();
            return;

            }
        });

        mCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(ChooseLogin.this, CustomerLogin.class);
            startActivity(intent);
            finish();
            return;

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
