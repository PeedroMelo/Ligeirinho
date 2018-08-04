package com.br.ligeirinho.ciclistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.ligeirinho.ChangeData;
import com.br.ligeirinho.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DriverSettings extends AppCompatActivity {

    LinearLayout changeNome, changeSobrenome, changeTelefone, changeEmail, changeSenha;
    TextView fieldNome, fieldSobrenome, fieldTelefone, fieldEmail, fieldSenha;

    private FirebaseAuth mAuth;
    private DatabaseReference mRequestDatabase;

    private String userID;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle("Dados Pessoais");

        // campos
        changeNome      = (LinearLayout) findViewById(R.id.changeNome);
        changeSobrenome = (LinearLayout) findViewById(R.id.changeSobrenome);
        changeTelefone  = (LinearLayout) findViewById(R.id.changeTelefone);
        changeEmail     = (LinearLayout) findViewById(R.id.changeEmail);
        changeSenha     = (LinearLayout) findViewById(R.id.changeSenha);

        fieldNome      = (TextView) findViewById(R.id.fieldNome);
        fieldSobrenome = (TextView) findViewById(R.id.fieldSobrenome);
        fieldTelefone  = (TextView) findViewById(R.id.fieldTelefone);
        fieldEmail     = (TextView) findViewById(R.id.fieldEmail);
        fieldSenha     = (TextView) findViewById(R.id.fieldSenha);



        mAuth            = FirebaseAuth.getInstance();
        userID           = mAuth.getCurrentUser().getUid();
        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("UserDetail").child(userID);

        getUserInfo();


        changeNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverSettings.this, ChangeData.class);
                intent.putExtra("userID",userID);
                intent.putExtra("nome",fieldNome.getText().toString());
                intent.putExtra("sobrenome","");
                intent.putExtra("telefone","");
                intent.putExtra("email","");
                intent.putExtra("senha","");
                startActivity(intent);
                finish();
                return;
            }
        });

        changeSobrenome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverSettings.this, ChangeData.class);
                intent.putExtra("userID",userID);
                intent.putExtra("nome","");
                intent.putExtra("sobrenome",fieldSobrenome.getText().toString());
                intent.putExtra("telefone","");
                intent.putExtra("email","");
                intent.putExtra("senha","");
                startActivity(intent);
                finish();
                return;
            }
        });

        changeTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverSettings.this, ChangeData.class);
                intent.putExtra("userID",userID);
                intent.putExtra("nome","");
                intent.putExtra("sobrenome","");
                intent.putExtra("telefone",fieldTelefone.getText().toString());
                intent.putExtra("email","");
                intent.putExtra("senha","");
                startActivity(intent);
                finish();
                return;
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverSettings.this, ChangeData.class);
                intent.putExtra("userID",userID);
                intent.putExtra("nome","");
                intent.putExtra("sobrenome","");
                intent.putExtra("telefone","");
                intent.putExtra("email",fieldEmail.getText().toString());
                intent.putExtra("senha","");
                startActivity(intent);
                finish();
                return;
            }
        });

        changeSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverSettings.this, ChangeData.class);
                intent.putExtra("userID",userID);
                intent.putExtra("email","");
                intent.putExtra("nome","");
                intent.putExtra("sobrenome","");
                intent.putExtra("telefone","");
                intent.putExtra("senha",fieldSenha.getText().toString());

                startActivity(intent);
                finish();
                return;
            }
        });






    }

    private void getUserInfo(){
        mRequestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("nome") != null){
                        nome = map.get("nome").toString();
                        fieldNome.setText(nome);
                    }
                    if(map.get("sobrenome") != null){
                        sobrenome = map.get("sobrenome").toString();
                        fieldSobrenome.setText(sobrenome);
                    }
                    if(map.get("telefone") != null){
                        telefone = map.get("telefone").toString();
                        fieldTelefone.setText(telefone);
                    }
                    if(map.get("email") != null){
                        email = map.get("email").toString();
                        fieldEmail.setText(email);
                    }
                    if(map.get("senha") != null){
                        senha = map.get("senha").toString();
                        fieldSenha.setText(senha);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, DriverAppSettings.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
