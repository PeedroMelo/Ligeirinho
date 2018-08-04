package com.br.ligeirinho.clientes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.br.ligeirinho.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomerNovoPedido extends AppCompatActivity {


    private EditText origem_complemento, destino_complemento, pDetalhes;
    private Button pPronto;

    private FirebaseAuth mAuth;
    private DatabaseReference mRequestDatabase;

    Long tsLong;

    private String requestID, userID, detalhes, origem, destino, origem_latlng, destino_latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_novo_pedido);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Verifica se o pedido já existe
        Intent intentPedido = getIntent();
        if(intentPedido.getStringExtra("requestID") != null && intentPedido.getStringExtra("requestID") != "") {
            requestID = intentPedido.getStringExtra("requestID");
        }else{
            // Equivalente a um RequestID (Rever melhor forma de fazer essa merda depois)
            Random random = new Random();
            requestID = String.valueOf(random.nextInt(999999999) + 1);
        }
        setTitle("Pedido #" + requestID);



        origem_complemento  = (EditText) findViewById(R.id.origem_complemento);
        destino_complemento = (EditText) findViewById(R.id.origem_complemento);
        pDetalhes           = (EditText) findViewById(R.id.detalhes_pedido);
        pPronto             = (Button) findViewById(R.id.pronto);

        // sExpress = (Switch) findViewById(R.id.swcServico);
        final String rStatus = "realizado";

        // Origem
        PlaceAutocompleteFragment autocompleteFragmentOrigem = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_origem);

        autocompleteFragmentOrigem.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                origem        = place.getName().toString();
                origem_latlng = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                Log.i("origem", "Ocorreu um erro: " + status);
            }
        });

        // Destino
        PlaceAutocompleteFragment autocompleteFragmentDetino = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_destino);

        autocompleteFragmentDetino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                destino        = place.getName().toString();
                destino_latlng = place.getLatLng().toString();
            }

            @Override
            public void onError(Status status) {
                Log.i("origem", "Ocorreu um erro: " + status);
            }
        });



        mAuth  = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        pPronto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(origem == ""){
//                    AlertDialog.Builder alert = new AlertDialog.Builder(CustomerNovoPedido.this);
//                        alert.setTitle("[001] Erro");
//                        alert.setMessage("Campo ORIGEM não pode ser vazio!");
//                        alert.create().show();
//
//                }
//
//                if(destino == ""){
//                    AlertDialog.Builder alert = new AlertDialog.Builder(CustomerNovoPedido.this);
//                        alert.setTitle("[002] Erro");
//                        alert.setMessage("Campo DESTINO não pode ser vazio!");
//                        alert.create().show();
//
//                }

                // Equivalente a um RequestID (Rever melhor forma de fazer essa merda depois)
                //Random RequestID = new Random();

                DatabaseReference customerRequest = FirebaseDatabase.getInstance().getReference("Pedidos").child("Realizados").child(userID).child(requestID);


                // Origem
                DatabaseReference origemRequest = customerRequest.child("origem");
                Map requestInfoOrigem = new HashMap();
                requestInfoOrigem.put("endereco", origem);
                requestInfoOrigem.put("complemento", origem_complemento.getText().toString());
                requestInfoOrigem.put("latlng", origem_latlng);


                // Destino
                DatabaseReference destinoRequest = customerRequest.child("destino");
                Map requestInfoDestino = new HashMap();
                requestInfoDestino.put("endereco", destino);
                requestInfoDestino.put("complemento", destino_complemento.getText().toString());
                requestInfoDestino.put("latlng", destino_latlng);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tsLong  = dateFormat.format(new Date());
                Map requestInfo = new HashMap();
                requestInfo.put("id_pedido", requestID.toString());
                requestInfo.put("id_usuario", userID.toString());
                requestInfo.put("detalhes", pDetalhes.getText().toString());
                requestInfo.put("status", rStatus);
                requestInfo.put("ts_pedido",tsLong.toString());
                requestInfo.put("origem", requestInfoOrigem);
                requestInfo.put("destino", requestInfoDestino);

                Log.d("request",requestInfoOrigem.toString());
                Log.d("request",requestInfoDestino.toString());
                Log.d("request",requestInfo.toString());

                origemRequest.updateChildren(requestInfoOrigem);
                destinoRequest.updateChildren(requestInfoDestino);
                customerRequest.updateChildren(requestInfo);

                avancarPagina(requestID.toString(), userID.toString(), pDetalhes.getText().toString());
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
        Intent intent = new Intent(this, CustomerMainActivity.class);
        startActivity(intent);
        finishAffinity();
        return;
    }

    public void avancarPagina(String requestID, String userID, String detalhes){
        Intent intent = new Intent(this, CustomerFinalizarPedido.class);
        intent.putExtra("requestID", requestID);
        intent.putExtra("userID", userID);
        intent.putExtra("detalhes", detalhes);
        startActivity(intent);
        finishAffinity();
        return;
    }
}
