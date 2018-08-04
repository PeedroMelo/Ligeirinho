package com.br.ligeirinho.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.br.ligeirinho.R;
import com.br.ligeirinho.models.Pedidos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CustomerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView cMainView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, cDatabase;

    String userID, nome;
    TextView viewNomeCompleto, viewEmail;

    Button bDetalhes;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pedidos em andamento");

        viewNomeCompleto = (TextView) findViewById(R.id.viewNomeCompleto);
        viewEmail        = (TextView) findViewById(R.id.viewEmail);

        FloatingActionButton novo_pedido = findViewById(R.id.novo_pedido);

        novo_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerMainActivity.this, CustomerNovoPedido.class);
                startActivity(intent);
                finishAffinity();
                return;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID);
        mDatabase.keepSynced(true);

        cMainView = (RecyclerView) findViewById(R.id.myrecycleviewer);
        cMainView.setLayoutManager(new LinearLayoutManager(this));

        loadPedidos();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.customer_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_meus_pedidos) {

        } else if (id == R.id.nav_config) {

            Intent intent = new Intent(this, CustomerAppSettings.class);
            startActivity(intent);
            finish();
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void loadPedidos(){

        FirebaseRecyclerAdapter<Pedidos,RequestViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pedidos, RequestViewHolder>
                (Pedidos.class, R.layout.request_row, RequestViewHolder.class, mDatabase) {
            @Override
            public void populateViewHolder(RequestViewHolder viewHolder, Pedidos model, int position) {
                viewHolder.setPedido(model.getId_pedido());
                viewHolder.setSolicitanteID(model.getId_usuario());
                viewHolder.setDetalhes(model.getDetalhes());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setTempoEntrega(model.getTempoEntrega());
                viewHolder.setDistancia(model.getDistancia());
                viewHolder.setValor(model.getValor());


                final String solicitanteNome = "";
                final String requestID = model.getId_pedido();

                cDatabase = FirebaseDatabase.getInstance().getReference("Users").child("UserDetail").child(userID);
                cDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                            if(map.get("nome") != null || map.get("sobrenome") != null){
                                nome = map.get("nome").toString() + " " + map.get("sobrenome").toString();
                                solicitanteNome.equals(nome);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.setSolicitante(solicitanteNome);


                Button btn = (Button) viewHolder.mView.findViewById(R.id.ver_mais);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CustomerMainActivity.this, CustomerDetalhesPedido.class);
                        intent.putExtra("requestID", requestID);
                        intent.putExtra("userID", userID);
                        intent.putExtra("solicitante", solicitanteNome);
                        startActivity(intent);
                        finish();
                        return;
                    }
                });

            }
        };

        cMainView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }


    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public RequestViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setPedido(String pedido){
            TextView vPedido = (TextView) mView.findViewById(R.id.vTitulo);
            vPedido.setText("#" + pedido);
        }
        public void setSolicitante(String solicitante){
            TextView vSollicitante = (TextView) mView.findViewById(R.id.fieldSolicitante);
            vSollicitante.setText(solicitante);
        }
        public void setSolicitanteID(String solicitanteID){
            TextView vSollicitanteID = (TextView) mView.findViewById(R.id.fieldSolicitanteID);
            vSollicitanteID.setText(solicitanteID);
        }
        public void setDetalhes(String detalhe){
            TextView vDetalhe = (TextView) mView.findViewById(R.id.fieldDetalhe);
            vDetalhe.setText(detalhe);
        }
        public void setStatus(String status){
            TextView vStatus = (TextView) mView.findViewById(R.id.fieldStatus);
            vStatus.setText(status);
        }
        public void setTempoEntrega(String tempoEntrega){
            TextView vTempoEntrega = (TextView) mView.findViewById(R.id.fieldTempoEntrega);
            vTempoEntrega.setText(tempoEntrega + " min");
        }
        public void setDistancia(String distancia){
            TextView vDistancia = (TextView) mView.findViewById(R.id.fieldDistancia);
            vDistancia.setText(distancia + " km");
        }
        public void setValor(String valor){
            TextView vValor = (TextView) mView.findViewById(R.id.fieldValor);
            vValor.setText("R$ " + valor);
        }


    }



}
