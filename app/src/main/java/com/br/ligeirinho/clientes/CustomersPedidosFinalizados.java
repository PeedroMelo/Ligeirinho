package com.br.ligeirinho.clientes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.br.ligeirinho.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class CustomersPedidosFinalizados extends AppCompatActivity {

    private RecyclerView cMainView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, cDatabase;

    String userID, nome;
    TextView viewNomeCompleto, viewEmail;

    Button bDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_pedidos_finalizados);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
}
