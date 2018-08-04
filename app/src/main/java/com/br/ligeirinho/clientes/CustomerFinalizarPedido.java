package com.br.ligeirinho.clientes;

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

public class CustomerFinalizarPedido extends AppCompatActivity {

    String requestID, userID, detalhes, nome, vTempoEntrega, vDistancia, vValor;
    Button confirmarPedido;
    TextView tempoEntrega, distancia, valor, solicitante, fieldDetalhes, tipoServico;

    private DatabaseReference mRequestDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_finalizar_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        solicitante   = (TextView) findViewById(R.id.solicitante);
        tipoServico   = (TextView) findViewById(R.id.tipoServico);
        fieldDetalhes = (TextView) findViewById(R.id.detalhes);
        tempoEntrega  = (TextView) findViewById(R.id.tempoEntrega);
        distancia     = (TextView) findViewById(R.id.distancia);
        valor         = (TextView) findViewById(R.id.valor);

        confirmarPedido = (Button) findViewById(R.id.confirmarPedido);


        Intent args = getIntent();
        requestID = args.getStringExtra("requestID");
        userID    = args.getStringExtra("userID");
        detalhes  = args.getStringExtra("detalhes");

        setTitle("Resumo do Pedido #" + requestID);


        mRequestDatabase = FirebaseDatabase.getInstance().getReference("Users").child("UserDetail").child(userID);
        mRequestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if(map.get("nome") != null || map.get("sobrenome") != null){
                        nome = map.get("nome").toString() + " " + map.get("sobrenome").toString();
                        solicitante.setText(nome);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tipoServico.setText("Comum");
        fieldDetalhes.setText(detalhes);

        // Supondo que aqui seja o calculo
        vTempoEntrega = "20";
        vDistancia = "5,1";
        vValor = "10,00";

        tempoEntrega.setText(vTempoEntrega + " min");
        distancia.setText(vDistancia + " km");
        valor.setText("R$ " + vValor);


        confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(CustomerFinalizarPedido.this);
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
        Intent intent = new Intent(this, CustomerNovoPedido.class);
        intent.putExtra("requestID", requestID);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void finalizou(){
        Intent intent = new Intent(this, CustomerMainActivity.class);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void updatePedido(){
        DatabaseReference customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);

        Map requestInfo = new HashMap();
        requestInfo.put("tempoEntrega", vTempoEntrega);
        requestInfo.put("distancia", vDistancia);
        requestInfo.put("valor", vValor);

        customerRequest.updateChildren(requestInfo);
        finalizou();
    }
}
