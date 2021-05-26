    package com.example.adminappcall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class EditContacto extends AppCompatActivity {

    EditText mNombreEditText;
    EditText mNumeroEditText;
    Uri url = Uri.parse("0");
    String uriAntigua;
    FirebaseUser user;
    ImageView mFotoImageView;
    Button mConfirmButton;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT=1;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String mKey= Objects.requireNonNull(getIntent().getExtras()).getString("key");
        String mSubuser= Objects.requireNonNull(getIntent().getExtras()).getString("subuser");


        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+mSubuser+"/contactos").child(mKey);
        mStorage = FirebaseStorage.getInstance().getReference(user.getUid());
        mNombreEditText = findViewById(R.id.nameEditText);
        mNumeroEditText = findViewById(R.id.NumeroEditText);
        mFotoImageView = findViewById(R.id.fotoImageView);
        mConfirmButton = findViewById(R.id.ConfirmarButton);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contacto contacto = new Contacto( (String)dataSnapshot.child("nombre").getValue() , String.valueOf(dataSnapshot.child("numero").getValue()), (String) dataSnapshot.child("url").getValue());
                mNombreEditText.setText(contacto.getNombre());
                mNumeroEditText.setText(contacto.getNumero());
                uriAntigua=contacto.getUrl();
                Picasso.get()
                        .load(contacto.getUrl())
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round)
                        .into(mFotoImageView);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditContacto.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mFotoImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        });

        mConfirmButton.setOnClickListener(this::onClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            assert data != null;
            Picasso.get()
                    .load(data.getData())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(mFotoImageView);
            url = data.getData();
        }



    }

    private void onClick(View v) {
        mDatabaseReference.child("nombre").setValue(mNombreEditText.getText().toString());
        mDatabaseReference.child("numero").setValue(mNumeroEditText.getText().toString());
        if (!String.valueOf(url).equals("0")){
            StorageReference    filepath= mStorage.child(url.getLastPathSegment());
            filepath.putFile(url).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> mDatabaseReference.child("url").setValue(uri.toString())));
        }
        finish();


    }
}