package com.br.ligeirinho.clientes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.br.ligeirinho.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CustomerDetalhesPedido extends AppCompatActivity {

    String requestID, userID, detalhes, nome, tempoEntrega, distancia, valor, status;
    Button confirmarPedido;
    TextView fieldTempoEntrega, fieldDistancia, fieldValor, fieldNome, fieldDetalhes, fieldTipoServico, fieldRealizado, fieldConfirmado, fieldFinalizado;

    private DatabaseReference mRequestDatabase,userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detalhes_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fieldNome          = (TextView) findViewById(R.id.solicitante);
        fieldTipoServico   = (TextView) findViewById(R.id.tipoServico);
        fieldDetalhes      = (TextView) findViewById(R.id.detalhes);
        fieldTempoEntrega  = (TextView) findViewById(R.id.tempoEntrega);
        fieldDistancia     = (TextView) findViewById(R.id.distancia);
        fieldValor         = (TextView) findViewById(R.id.valor);
        fieldRealizado     = (TextView) findViewById(R.id.realizado);
        fieldConfirmado    = (TextView) findViewById(R.id.confirmado);
        fieldFinalizado    = (TextView) findViewById(R.id.finalizado);

        Intent intent = getIntent();
        userID    = intent.getStringExtra("userID");
        requestID = intent.getStringExtra("requestID");

        setTitle("#" + requestID);

        getUserInfo();

        getRequestInfo();


    }

    @Override
    public void onBackPressed() { voltarPagina();  }

    @Override
    public boolean onSupportNavigateUp() {
        voltarPagina();
        return true;
    }

    public void voltarPagina(){
        Intent intent = new Intent(this, CustomerMainActivity.class);
        startActivity(intent);
        finishAffinity();
        return;
    }


    public void getUserInfo(){

        userData = FirebaseDatabase.getInstance().getReference("Users").child("UserDetail").child(userID);
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("nome") != null){
                    nome = map.get("nome").toString();
                    fieldNome.setText(nome);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getRequestInfo(){
        mRequestDatabase = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);
        mRequestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("detalhes") != null){
                    detalhes = map.get("detalhes").toString();
                    fieldDetalhes.setText(detalhes);
                }

                if(map.get("tempoEntrega") != null){
                    tempoEntrega = map.get("tempoEntrega").toString();
                    fieldTempoEntrega.setText(tempoEntrega + " min");
                }

                if(map.get("distancia") != null){
                    distancia = map.get("distancia").toString();
                    fieldDistancia.setText(distancia + " km");
                }

                if(map.get("valor") != null){
                    valor = map.get("valor").toString();
                    fieldValor.setText("R$ " + valor);
                }

                if(map.get("status") != null){
                    status = map.get("status").toString();

                    if(status.equals("realizado")){
                        statusAtivo(fieldRealizado);
                    }

                    if(status.equals("confirmado")){
                        statusAtivo(fieldRealizado);
                        statusAtivo(fieldConfirmado);
                    }

                    if(status.equals("finalizado")){
                        statusAtivo(fieldRealizado);
                        statusAtivo(fieldConfirmado);
                        statusAtivo(fieldFinalizado);
                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            @SuppressLint("ResourceAsColor")
            public void statusAtivo(TextView field){
                field.setTextSize(24);
                field.setTextColor(R.color.colorPrimaryDark);
            }

            @SuppressLint("ResourceAsColor")
            public void statusInativo(TextView field){
                field.setTextSize(16);
                field.setTextColor(R.color.colorSecondaryText);
            }
        });


    }


}
