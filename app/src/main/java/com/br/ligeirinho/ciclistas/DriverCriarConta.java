package com.br.ligeirinho.ciclistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.br.ligeirinho.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DriverCriarConta extends AppCompatActivity {

    private ImageButton voltar;
    private Button criarConta;
    private EditText fieldNome, fieldSobrenome, fieldEmail, fieldTelefone, fieldSenha;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_criar_conta);
        getSupportActionBar().hide();

        fieldNome      = (EditText) findViewById(R.id.fieldNome);
        fieldSobrenome = (EditText) findViewById(R.id.fieldSobrenome);
        fieldEmail     = (EditText) findViewById(R.id.fieldEmail);
        fieldTelefone  = (EditText) findViewById(R.id.fieldTelefone);
        fieldSenha     = (EditText) findViewById(R.id.fieldSenha);

        criarConta = (Button) findViewById(R.id.criar_conta);

        voltar = (ImageButton) findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverCriarConta.this, DriverLogin.class);
                startActivity(intent);
                finish();
            }
        });

        // Registra ciclista
        criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome      = fieldNome.getText().toString();
                String sobrenome = fieldSobrenome.getText().toString();
                String telefone  = fieldEmail.getText().toString();
                String email     = fieldEmail.getText().toString();
                String senha     = fieldSenha.getText().toString();

                if (nome.equals("") || sobrenome.equals("") || telefone.equals("") || email.equals("") || senha.equals("")){
                    Toast.makeText(DriverCriarConta.this, "[001] Ops! Faltou preecher os campos", Toast.LENGTH_LONG).show();
                    return;
                }

                try{

                    Auth = FirebaseAuth.getInstance();
                    Auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(DriverCriarConta.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(DriverCriarConta.this, "[002] Ocorreu um erro na criação de usuário", Toast.LENGTH_LONG).show();
                            }else{
                                String user_id = Auth.getCurrentUser().getUid();
                                DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                                current_user.setValue(true);

                                // Atualiza os detalhes do usuario
                                updateDados(user_id);

                                // Redireciona para a tela principal
                                finalizou();
                            }
                        }
                    });

                }catch (Exception e){
                    Toast.makeText(DriverCriarConta.this, "[003] Deu ruim aí: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() { voltarPagina();  }

    public void voltarPagina(){
        Intent intent = new Intent(this, DriverLogin.class);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void finalizou(){
        Intent intent = new Intent(this, DriverMainActivity.class);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void updateDados(String userID){
        DatabaseReference userDetail = FirebaseDatabase.getInstance().getReference("Users").child("UserDetail").child(userID);

        Map requestInfo = new HashMap();
        requestInfo.put("nome", fieldNome.getText().toString());
        requestInfo.put("sobrenome", fieldSobrenome.getText().toString());
        requestInfo.put("telefone", fieldTelefone.getText().toString());

        userDetail.updateChildren(requestInfo);
    }
}
