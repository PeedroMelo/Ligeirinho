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

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
        getSupportActionBar().hide();

 //       redirectUser();

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
        //mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void redirectUser(){

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance(); // Recupera a instancia do Firebase e seta na cAuth

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Verifica o status do usuário
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Guarda a informação do usuário logado
                //if(user.getUid() != null) Log.d("user-logado", user.getUid());

                // Redireciona se estiver vazio
                if ( user != null ){
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user.getUid());

                    if(current_user_db.getParent().getKey().equals("Customers")){
                        Intent intent = new Intent(ChooseLogin.this, CustomerMainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }

                    if(current_user_db.getParent().getKey().equals("Customers")){

                        Intent intent = new Intent(ChooseLogin.this, DriverMainActivity.class);
                        startActivity(intent);
                        finish();
                        return;

                    }
                }

            }
        };

    }
}
