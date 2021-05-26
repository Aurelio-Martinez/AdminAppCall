package com.example.adminappcall;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import java.util.Objects;


public class MainActivityMedicina extends AppCompatActivity {

    public DatabaseReference mDatabaseReference;
    LinearLayoutManager mLayoutManager;
    Intent origin;
    RecyclerView mRecyclerView;
    MedicinaAdapter mAdapterMedicinas;
    private ArrayList<Medicina> mMedicinas;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicacion);
        origin = getIntent();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mMedicinas = new ArrayList<>();


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
        }

        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/" + origin.getStringExtra("key") + "/medicinas");

        mRecyclerView = findViewById(R.id.contactoRecyclerView);
        findViewById(R.id.añadir).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMedicina.class);
            intent.putExtra("subuser", origin.getStringExtra("key"));
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Recycler();
    }

    public void Recycler() {


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapterMedicinas = new MedicinaAdapter(mMedicinas);
        mRecyclerView.setAdapter(mAdapterMedicinas);
        Content();
        deleteSwipe();
    }

    private void Content() {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMedicinas.clear();
                for ( @NonNull DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Medicina medicina = new Medicina((String) postSnapshot.child("nombre").getValue(),
                            String.valueOf(postSnapshot.child("dosis").getValue()), (String) postSnapshot.child("url").getValue(),
                            postSnapshot.getKey(),
                            (Long)postSnapshot.child("dia").getValue(),
                            (Long)postSnapshot.child("mes").getValue(),
                            (Long)postSnapshot.child("año").getValue(),
                            Objects.requireNonNull(postSnapshot.child("horario").getValue()).toString());
                    mMedicinas.add(medicina);
                }
                mAdapterMedicinas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivityMedicina.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void deleteSwipe() {
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityMedicina.this);
                builder.setCancelable(true);
                builder.setTitle("Eliminar");
                builder.setMessage("Desea Eliminar El Contacto?");
                builder.setPositiveButton("Confirmar",
                        (dialog, which) -> {
                            mDatabaseReference.child(mMedicinas.get(viewHolder.getAdapterPosition()).getKey()).setValue(null);
                            mAdapterMedicinas.deleteItem(viewHolder.getAdapterPosition());
                            mAdapterMedicinas.notifyDataSetChanged();
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                }).create().show();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
