package com.example.adminappcall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivitySubuser extends AppCompatActivity {

    public DatabaseReference mDatabaseReference;
    Intent origin;
    FirebaseUser user;
    SwitchCompat ciego;
    TextView nombre, edad, genero;
    ImageView mFotoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subuser);
        origin = getIntent();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }

        mFotoImageView= findViewById(R.id.Foto);
        nombre=findViewById(R.id.Nombre);
        edad=findViewById(R.id.edad);
        genero =findViewById(R.id.genero);

        nombre.setText(origin.getStringExtra("name"));
        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+origin.getStringExtra("key"));
        ciego=findViewById(R.id.switch1);
        ciego.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (ciego.isChecked()) {
                FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/" + origin.getStringExtra("key") + "/ciego").setValue("si");
            } else {
                FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/" + origin.getStringExtra("key") + "/ciego").setValue("no");
            }
        });

        findViewById(R.id.calendario).setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivityCalendar.class);
            intent.putExtra("key",origin.getStringExtra("key"));
            startActivity(intent);
        });

        findViewById(R.id.medicinas).setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivityMedicina.class);
            intent.putExtra("key",origin.getStringExtra("key"));
            startActivity(intent);
        });



        findViewById(R.id.contactos).setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivityContactos.class);
            intent.putExtra("key",origin.getStringExtra("key"));
            startActivity(intent);
        });
        findViewById(R.id.Mails).setOnClickListener(v -> {
            Intent intent = new Intent(this,EmailsActivity.class);
            intent.putExtra("key",origin.getStringExtra("key"));
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Content();
    }


    private void Content() {

        mDatabaseReference.child("ciego").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ciego.setChecked(Objects.requireNonNull(dataSnapshot.getValue()).toString().equals("si"));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ciego.setChecked(false);
            }
        });
        mDatabaseReference.child("edad").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edad.setText("Edad: "+ Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ciego.setChecked(false);
            }
        });
        mDatabaseReference.child("genero").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                genero.setText("Genero: "+ Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ciego.setChecked(false);
            }
        });
        mDatabaseReference.child("foto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Picasso.get()
                            .load((String)Objects.requireNonNull(dataSnapshot).getValue())
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round)
                            .into(mFotoImageView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ciego.setChecked(false);
            }
        });



    }
}