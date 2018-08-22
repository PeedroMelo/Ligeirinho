package com.br.ligeirinho;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.br.ligeirinho.ciclistas.DriverMainActivity;
import com.br.ligeirinho.clientes.CustomerMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        redirectUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void redirectUser(){

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance(); // Recupera a instancia do Firebase e seta na cAuth

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Verifica o status do usuário
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Guarda a informação do usuário logado

                // Redireciona se não estiver vazio
                if ( user != null ){
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user.getUid());
                    current_user_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                Intent intent = new Intent(Splash.this, CustomerMainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }else{
                                Intent intent = new Intent(Splash.this, DriverMainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Splash.this, ChooseLogin.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }, 3000);
                }

            }
        };

    }
}
