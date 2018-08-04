package com.br.ligeirinho.ciclistas;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.br.ligeirinho.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DriverFinalizarPedido extends AppCompatActivity {

    String requestID, userID, pDetalhes, pTempoEntrega, pDistancia, pValor, pStatus, pTsPedido, pTsFinalizado, latlng, lat_lng;
    SimpleDateFormat dateFormat;
    Button buttonOrigemMapa, buttonDestinoMapa, buttonCancelar, buttonEntregar;

    DatabaseReference customerRequest, destinoRequest, origemRequest, mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_finalizar_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        requestID = intent.getStringExtra("requestID");
        userID = intent.getStringExtra("solicitanteID");

        setTitle("#" + requestID);

        buttonOrigemMapa  = (Button) findViewById(R.id.buttonOrigemMapa);
        buttonDestinoMapa = (Button) findViewById(R.id.buttonDestinoMapa);
        buttonCancelar    = (Button) findViewById(R.id.buttonCancelar);
        buttonEntregar    = (Button) findViewById(R.id.buttonEntregar);


        buttonOrigemMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong("origem");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlng+"&mode=b");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        buttonDestinoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong("destino");
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlng+"&mode=b");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DriverFinalizarPedido.this);
                dialog.setCancelable(false);
                dialog.setTitle("Cancelar Pedido");
                dialog.setMessage("Deseja mesmo cancelar o pedido?");
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        buttonEntregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DriverFinalizarPedido.this);
                dialog.setCancelable(false);
                dialog.setTitle("Entregar Pedido");
                dialog.setMessage("Tudo certo na entrega?");
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

        // Atualiza Status
        atualizaStatusReuest();

        // Grava na Fila de Status Finalizado
        gravaStatusFinalizado();


    }

    public void atualizaStatusReuest(){

        customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);

        Map requestInfo = new HashMap();
        requestInfo.put("status", "finalizado");

        customerRequest.updateChildren(requestInfo);

    }

    public void gravaStatusFinalizado(){

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    pDetalhes     = map.get("detalhes").toString();
                    pStatus       = map.get("status").toString();
                    pTsPedido     = map.get("ts_pedido").toString();
                    dateFormat    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    pTsFinalizado = dateFormat.format(new Date());


                    HashMap requestInfoOrigem, requestInfoDestino;

                    requestInfoOrigem = (HashMap) map.get("origem");
                    requestInfoDestino = (HashMap) map.get("destino");

                    Log.d("endereco-certinho", requestInfoOrigem.get("endereco").toString());



                    customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Finalizados").child(userID).child(requestID);

                    Map requestInfo = new HashMap();
                    requestInfo.put("id_pedido", requestID);
                    requestInfo.put("id_usuario", userID);
                    requestInfo.put("detalhes", pDetalhes);
                    requestInfo.put("status", pStatus);
                    requestInfo.put("ts_pedido",pTsPedido);
                    requestInfo.put("ts_finalizado",pTsFinalizado);
                    requestInfo.put("tempoEntrega", pTempoEntrega);
                    requestInfo.put("distancia", pDistancia);
                    requestInfo.put("valor", pValor);
                    requestInfo.put("origem",requestInfoOrigem);
                    requestInfo.put("destino",requestInfoDestino);


                    try {
                        customerRequest.updateChildren(requestInfo);
                    }catch (Exception e){
                        Log.e("[001] Erro ao inserir", e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        removePedidoFinalizadoConfirmado();
        finalizou();

    }

    private void removePedidoFinalizadoConfirmado(){
        customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Finalizados").child(userID).child(requestID);
        customerRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);
                    try {
                        mDatabaseReference.removeValue();
                    }catch (Exception e){
                        Log.e("[001] Erro ao inserir", e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void finalizou() {
        Intent intent = new Intent(DriverFinalizarPedido.this, DriverMainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void getLatLong(String oriDes){

        final String tipo = oriDes;
        String retorno = "";

        customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);
        customerRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    HashMap requestInfo;

                    requestInfo = (HashMap) map.get(tipo);
                    latlng = requestInfo.get("latlng").toString().replace("lat/lng: (","").replace(")","");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
