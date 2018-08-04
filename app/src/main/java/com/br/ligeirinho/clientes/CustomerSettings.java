package com.br.ligeirinho.clientes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.ligeirinho.ChangeData;
import com.br.ligeirinho.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CustomerSettings extends AppCompatActivity {

    LinearLayout changeNome, changeSobrenome, changeTelefone, changeEmail, changeSenha;
    TextView fieldNome, fieldSobrenome, fieldTelefone, fieldEmail, fieldSenha;

    ImageView imagemPerfil;

    private FirebaseAuth mAuth;
    private DatabaseReference mRequestDatabase;

    private String userID;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String email;
    private String senha;
    private String imagem_perfil;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        setTitle("Dados Pessoais");

        // campos
        changeNome      = (LinearLayout) findViewById(R.id.changeNome);
        changeSobrenome = (LinearLayout) findViewById(R.id.changeSobrenome);
        changeTelefone  = (LinearLayout) findViewById(R.id.changeTelefone);

        fieldNome      = (TextView) findViewById(R.id.fieldNome);
        fieldSobrenome = (TextView) findViewById(R.id.fieldSobrenome);
        fieldTelefone  = (TextView) findViewById(R.id.fieldTelefone);

        imagemPerfil = (ImageView) findViewById(R.id.imagemPerfil);

        mAuth            = FirebaseAuth.getInstance();
        userID           = mAuth.getCurrentUser().getUid();
        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("UserDetail").child(userID);

        getUserInfo();

        changeNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerSettings.this, ChangeData.class);
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
                Intent intent = new Intent(CustomerSettings.this, ChangeData.class);
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
                Intent intent = new Intent(CustomerSettings.this, ChangeData.class);
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

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
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
                    if(map.get("imagem_perfil") != null){
                        imagem_perfil = map.get("imagem_perfil").toString();
                        Glide.with(getApplication()).load(imagem_perfil).into(imagemPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = intent.getData();
            resultUri = imageUri;
            imagemPerfil.setImageURI(resultUri);

            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(userID);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (Exception e){
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map newImage = new HashMap();
                    newImage.put("imagem_perfil", downloadUrl.toString());

                    mRequestDatabase.updateChildren(newImage);
                    finish();

                }
            });
        }else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, CustomerAppSettings.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
