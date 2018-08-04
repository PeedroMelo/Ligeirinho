package com.br.ligeirinho.ciclistas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.br.ligeirinho.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DriverVisualizarPedido extends AppCompatActivity {

    Button buttonIniciar;
    TextView vSolicitante, vDetalhes, vTempoEntrega, vDistancia, vValor;

    String requestID, userID, nome, detalhes, tempoEntrega, distancia, valor;

    DatabaseReference mRequestDatabase, userData, customerRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_visualizar_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        requestID = intent.getStringExtra("requestID");
        userID = intent.getStringExtra("solicitanteID");

        setTitle("#" + requestID);

        vSolicitante  = (TextView) findViewById(R.id.fieldSolicitante);
        vDetalhes     = (TextView) findViewById(R.id.fieldDetalhes);
        vTempoEntrega = (TextView) findViewById(R.id.fieldTempoEntrega);
        vDistancia    = (TextView) findViewById(R.id.fieldDistancia);
        vValor        = (TextView) findViewById(R.id.fieldValor);

        getRequestInfo();
        getUserInfo();

        buttonIniciar = (Button) findViewById(R.id.iniciar);
        buttonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DriverVisualizarPedido.this);
                dialog.setCancelable(false);
                dialog.setTitle("Finalizar Pedido");
                dialog.setMessage("Deseja finalizar o pedido agora?");
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePedido();
                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });



    }

    @Override
    public void onBackPressed() { voltarPagina();  }

    @Override
    public boolean onSupportNavigateUp() {
        voltarPagina();
        return true;
    }

    public void voltarPagina(){
        Intent intent = new Intent(this, DriverMainActivity.class);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void updatePedido(){
        customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);

        Map requestInfo = new HashMap();
        requestInfo.put("status", "confirmado");

        customerRequest.updateChildren(requestInfo);
        finalizou();
    }

    private void finalizou() {
        Intent intent = new Intent(DriverVisualizarPedido.this, DriverMainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void getUserInfo(){

        userData = FirebaseDatabase.getInstance().getReference("Users").child("UserDetail").child(userID);
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("nome") != null && map.get("sobrenome") != null){
                    nome = map.get("nome").toString() + " " + map.get("sobrenome").toString();
                    vSolicitante.setText(nome);
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
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.get("detalhes") != null) {
                        detalhes = map.get("detalhes").toString();
                        vDetalhes.setText(detalhes);
                    }

                    if (map.get("tempoEntrega") != null) {
                        tempoEntrega = map.get("tempoEntrega").toString();
                        vTempoEntrega.setText(tempoEntrega + " min");
                    }

                    if (map.get("distancia") != null) {
                        distancia = map.get("distancia").toString();
                        vDistancia.setText(distancia + " km");
                    }

                    if (map.get("valor") != null) {
                        valor = map.get("valor").toString();
                        vValor.setText("R$ " + valor);
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
