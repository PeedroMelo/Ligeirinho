package com.br.ligeirinho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.br.ligeirinho.ciclistas.DriverSettings;
import com.br.ligeirinho.clientes.CustomerSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangeData extends AppCompatActivity {

    ImageButton voltar;
    TextView fieldLabel;
    EditText fieldData;
    Button salvar_alteracoes;

    String userID, nome, sobrenome, telefone, email, senha, telaAnterior, key;

    private FirebaseAuth cAuth;
    private DatabaseReference cCustomerDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        getSupportActionBar().hide();

        Intent args = getIntent();

        userID    = args.getStringExtra("userID");
        nome      = args.getStringExtra("nome");
        sobrenome = args.getStringExtra("sobrenome");
        telefone  = args.getStringExtra("telefone");
        email     = args.getStringExtra("email");
        senha     = args.getStringExtra("senha");

        telaAnterior = args.getStringExtra("tela");

        setTitle("Alterar Dados");

        fieldLabel        = (TextView) findViewById(R.id.fieldLabel);
        fieldData         = (EditText) findViewById(R.id.fieldData);
        salvar_alteracoes = (Button) findViewById(R.id.salvar_alteracoes);

        voltar = (ImageButton) findViewById(R.id.voltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarPagina(telaAnterior);
            }
        });

        if(!nome.isEmpty()){
            key = "nome";
            fieldLabel.setText("Nome");
            fieldData.setText(nome);
            fieldData.setSelection(fieldData.getText().length());
        }
        if(!sobrenome.isEmpty()){
            key = "sobrenome";
            fieldLabel.setText("Sobrenome");
            fieldData.setText(sobrenome);
            fieldData.setSelection(fieldData.getText().length());
        }
        if(!telefone.isEmpty()){
            key = "telefone";
            fieldLabel.setText("Telefone");
            fieldData.setText(telefone);
            fieldData.setSelection(fieldData.getText().length());
        }
        if(!email.isEmpty()){
            key = "email";
            fieldLabel.setText("Email");
            fieldData.setText(email);
            fieldData.setSelection(fieldData.getText().length());
        }
        if(!senha.isEmpty()){
            key = "senha";
            fieldLabel.setText("Senha");
            fieldData.setHint("Digite sua senha");
            fieldData.setSelection(fieldData.getText().length());
            fieldData.setInputType(128);
        }


        salvar_alteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fieldData.getText().toString().isEmpty()){
                    Toast.makeText(ChangeData.this, "[001] O campo não pode estar vazio", Toast.LENGTH_SHORT);
                    return;
                }

                cCustomerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("UserDetail").child(userID);

                Log.d("key", key);
                Log.d("val", fieldData.getText().toString());

                Map userInfo = new HashMap();
                userInfo.put(key, fieldData.getText().toString());

                cCustomerDatabaseReference.updateChildren(userInfo);

                voltarPagina(telaAnterior);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        voltarPagina(telaAnterior);

    }

    public void voltarPagina(String voltarTela){

        if(voltarTela.equals("ciclista")) {
            Intent intent = new Intent(this, DriverSettings.class);
            startActivity(intent);
            finishAffinity();
            return;
        }else{
            Intent intent = new Intent(this, CustomerSettings.class);
            startActivity(intent);
            finishAffinity();
            return;
        }
    }
}
