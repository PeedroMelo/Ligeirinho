package com.br.ligeirinho.clientes;

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

public class CustomerLogin extends AppCompatActivity {

    private EditText cEmail, cPassword;
    private Button cLogin, cSignin;

    private FirebaseAuth cAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Login | Cliente");

        // Firebase Auth
        cAuth = FirebaseAuth.getInstance(); // Recupera a instancia do Firebase e seta na cAuth

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Verifica o status do usuário
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Guarda a informação do usuário logado

                // Redireciona se estiver vazio
                if ( user != null ){
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user.getUid());
                    Log.d("user-logado", current_user_db.getParent().getKey().toString());

                    if(current_user_db.getParent().getKey().equals("Customers")){
                        Intent intent = new Intent(CustomerLogin.this, CustomerMainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }

            }
        };

        // Fields & Buttons
        cEmail    = (EditText) findViewById(R.id.email);
        cPassword = (EditText) findViewById(R.id.password);

        cLogin  = (Button) findViewById(R.id.login);
        cSignin = (Button) findViewById(R.id.signin);

        cSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerLogin.this, CriarConta.class);
                intent.putExtra("tipo", "cliente");
                startActivity(intent);
                finishAffinity();
            }
        });

        // Registra ciclista
//        cSignin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (cEmail == null || cPassword == null){
//                    Toast.makeText(CustomerLogin.this, "Campos Obrigatórios!", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                final String email = cEmail.getText().toString();
//                final String password = cPassword.getText().toString();
//
//
//                cAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()){
//                            Toast.makeText(CustomerLogin.this, "Ocorreu um erro na criação de usuário", Toast.LENGTH_LONG).show();
//                        }else{
//                            String user_id = cAuth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
//                            current_user_db.setValue(true);
//                        }
//                    }
//                });
//            }
//        });


        cLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = cEmail.getText().toString();
                final String password = cPassword.getText().toString();

                cAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(CustomerLogin.this, "Ocorreu um erro no login", Toast.LENGTH_LONG).show();
                        }else{
                            String user_id = cAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
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
        cAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cAuth.removeAuthStateListener(firebaseAuthListener);
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
