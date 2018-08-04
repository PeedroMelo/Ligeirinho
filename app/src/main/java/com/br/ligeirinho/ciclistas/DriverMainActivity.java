package com.br.ligeirinho.ciclistas;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.br.ligeirinho.R;
import com.br.ligeirinho.fragments.MapFragment;
import com.br.ligeirinho.models.Pedidos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap googleMap;

    Location ultimaLocalizacao;
    LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private RecyclerView cMainView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, cDatabase;

    String customerID, driverID, nome;

    Button detalhes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pedidos em andamento");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados");
        Log.d("pedidos", mDatabase.toString());
        mDatabase.keepSynced(true);

        cMainView = (RecyclerView) findViewById(R.id.myrecycleviewer);
        cMainView.setLayoutManager(new LinearLayoutManager(this));

        loadPedidos();
        Log.d("m-database",mDatabase.getRef().toString());
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
        getMenuInflater().inflate(R.menu.driver_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rides) {

        } else if (id == R.id.nav_settings) {

            Intent intent = new Intent(this, DriverAppSettings.class);
            startActivity(intent);
            finish();
            return true;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void loadPedidos(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    FirebaseRecyclerAdapter<Pedidos, DriverMainActivity.RequestViewHolder> firebaseRecyclerAdapter = null;

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        DatabaseReference lDatabase = mDatabase.child(data.getKey());


                        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pedidos, DriverMainActivity.RequestViewHolder>
                                (Pedidos.class, R.layout.request_row, DriverMainActivity.RequestViewHolder.class, lDatabase) {
                            @Override
                            public void populateViewHolder(DriverMainActivity.RequestViewHolder viewHolder, final Pedidos model, int position) {
                                viewHolder.setPedido(model.getId_pedido());
                                viewHolder.setSolicitanteID(model.getId_usuario());
                                viewHolder.setDetalhes(model.getDetalhes());
                                viewHolder.setStatus(model.getStatus());
                                viewHolder.setTempoEntrega(model.getTempoEntrega());
                                viewHolder.setDistancia(model.getDistancia());
                                viewHolder.setValor(model.getValor());

                                final String requestID = model.getId_pedido();
                                final String solicitanteID = model.getId_usuario();
                                final String statusPedido = model.getStatus();


                                Button btn = (Button) viewHolder.mView.findViewById(R.id.ver_mais);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Class activityRedir = DriverVisualizarPedido.class;
                                        if (!statusPedido.equals("realizado")) {
                                            activityRedir = DriverFinalizarPedido.class;
                                        }

                                        Intent intent = new Intent(DriverMainActivity.this, activityRedir);
                                        intent.putExtra("requestID", requestID);
                                        intent.putExtra("solicitanteID", solicitanteID);
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                });

                            }
                        };
                    }

                    cMainView.setAdapter(firebaseRecyclerAdapter);
                    firebaseRecyclerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

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
//        public void setSolicitante(String solicitante){
//            TextView vSollicitante = (TextView) mView.findViewById(R.id.fieldSolicitante);
//            vSollicitante.setText(solicitante);
//        }
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
            vValor.setText("R$" + valor);
        }

    }




}
