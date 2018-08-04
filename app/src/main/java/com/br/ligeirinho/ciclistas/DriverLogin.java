package com.br.ligeirinho.ciclistas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.ligeirinho.ChooseLogin;
import com.br.ligeirinho.CriarConta;
import com.br.ligeirinho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLogin extends AppCompatActivity {

    private EditText dEmail, dPassword;
    private Button dLogin, dSignin;

    private FirebaseAuth dAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Login | Ciclsita");

        // Firebase Auth
        dAuth = FirebaseAuth.getInstance(); // Recupera a instancia do Firebase e seta na dAuth

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Verifica o status do usuário
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Guarda a informação do usuário logado

                // Redireciona se estiver vazio
                if ( user != null ){
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user.getUid());
                    Log.d("user-logado", current_user_db.getParent().getKey().toString());


                    if(current_user_db.getParent().getKey().equals("Drivers")){
                        Intent intent = new Intent(DriverLogin.this, DriverMainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }

            }
        };

        // Fields & Buttons
        dEmail    = (EditText) findViewById(R.id.email);
        dPassword = (EditText) findViewById(R.id.password);

        dLogin  = (Button) findViewById(R.id.login);
        dSignin = (Button) findViewById(R.id.signin);

        dSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverLogin.this, CriarConta.class);
                intent.putExtra("tipo", "ciclista");
                startActivity(intent);
                finishAffinity();
            }
        });

        // Registra ciclista
//        dSignin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final String email = dEmail.getText().toString();
//                final String password = dPassword.getText().toString();
//
//                if (email == null || password == null){
//                    Toast.makeText(DriverLogin.this, "Campos Obrigatórios!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                dAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogin.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(DriverLogin.this, "Ocorreu um erro na criação de usuário", Toast.LENGTH_LONG).show();
//                        }else{
//                            String user_id = dAuth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
//                            current_user_db.setValue(true);
//                        }
//                    }
//                });
//            }
//        });


        dLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = dEmail.getText().toString();
                final String password = dPassword.getText().toString();

                dAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(DriverLogin.this, "Ocorreu um erro no login", Toast.LENGTH_LONG).show();
                        }else{
                            String user_id = dAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        dAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
        voltarPagina();
    }

    @Override
    public boolean onSupportNavigateUp() {
        voltarPagina();
        return true;
    }

    public void voltarPagina(){
        Intent intent = new Intent(this, ChooseLogin.class);
        startActivity(intent);
        finishAffinity();
        return;
    }
}
