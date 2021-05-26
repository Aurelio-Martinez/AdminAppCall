package com.example.adminappcall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public DatabaseReference mDatabaseReference;
    LinearLayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    SubuserAdapter mAdaperSubuser;
    private ArrayList<Subusuario> mSubusers;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent(MainActivity.this,LogInActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        mSubusers = new ArrayList<>();
        findViewById(R.id.añadir).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AddSubuser.class);
            startActivity(intent);
        });
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }
        mRecyclerView= findViewById(R.id.contactoRecyclerView);
        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/"+user.getUid()+"/subusers");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Recycler();
    }

    public void Recycler() {


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdaperSubuser = new SubuserAdapter(mSubusers);
        mRecyclerView.setAdapter(mAdaperSubuser);
        Content();
    }

    private void Content() {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSubusers.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Subusuario contacto = new Subusuario( (String)postSnapshot.getValue() , postSnapshot.getKey());
                    mSubusers.add(contacto);
                }
                mAdaperSubuser.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }



}