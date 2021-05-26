package com.example.adminappcall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.Objects;


public class AddContacto extends AppCompatActivity {

    EditText mNombreEditText;
    EditText mNumeroEditText;
    ImageView mFotoImageView;
    Uri url = Uri.parse("0");
    Button mConfirmButton;
    FirebaseUser user;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT=1;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contacto);


        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mNombreEditText = findViewById(R.id.nameEditText);
        mNumeroEditText = findViewById(R.id.NumeroEditText);
        mFotoImageView = findViewById(R.id.fotoImageView);
        mConfirmButton = findViewById(R.id.ConfirmarButton);
        String mSubuser= Objects.requireNonNull(getIntent().getExtras()).getString("subuser");



        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+mSubuser+"/contactos");
        mStorage = FirebaseStorage.getInstance().getReference(user.getUid());

        mFotoImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,GALLERY_INTENT);
        });

        
        mConfirmButton.setOnClickListener(v -> {
            String name = mNombreEditText.getText().toString();
            String numero = mNumeroEditText.getText().toString();
            String id = mDatabaseReference.push().getKey();
            assert id != null;
            mDatabaseReference =   mDatabaseReference.child(id);
            mDatabaseReference.child("nombre").setValue(name);
            mDatabaseReference.child("numero").setValue(numero);
            if (!String.valueOf(url).equals("0")){
                StorageReference    filepath= mStorage.child(url.getLastPathSegment());
                filepath.putFile(url).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> mDatabaseReference.child("url").setValue(uri.toString())));
            }else{
                mDatabaseReference.child("url").setValue(url.toString());
            }
            finish();
        });
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
}