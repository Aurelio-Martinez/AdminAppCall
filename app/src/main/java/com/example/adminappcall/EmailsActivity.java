package com.example.adminappcall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class EmailsActivity extends AppCompatActivity {

    public DatabaseReference mDatabaseReference;


    LinearLayoutManager mLayoutManager;
    AdapterMail mAdapterMail;
    RecyclerView mRecyclerView;
    private ArrayList<Mail> mMails;
    Intent origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        origin = getIntent();
        setContentView(R.layout.activity_email);

        findViewById(R.id.añadir).setOnClickListener(v -> {

            LayoutInflater linf = LayoutInflater.from(this);
            final View inflator = linf.inflate(R.layout.add_email, null);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(inflator);
            final EditText et1 = inflator.findViewById(R.id.username);
            alert.setPositiveButton("Confirmar", (dialog, whichButton) -> mDatabaseReference.child(Objects.requireNonNull(mDatabaseReference.push().getKey())).setValue(et1.getText().toString()));
            alert.setNegativeButton("Cancelar", (dialog, whichButton) -> dialog.cancel());
            alert.show();
        });


        mMails = new ArrayList<>();
        mRecyclerView= findViewById(R.id.mailRecyclerView);
        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+origin.getStringExtra("key")+"/mails");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Recycler();
        deleteSwipe();
    }

    public void Recycler() {

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapterMail = new AdapterMail(mMails);
        mRecyclerView.setAdapter(mAdapterMail);
        Content();
    }

    private void Content() {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMails.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Mail mail = new Mail( (String)postSnapshot.getValue() , postSnapshot.getKey());
                    mMails.add(mail);
                }
                mAdapterMail.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

                AlertDialog.Builder builder = new AlertDialog.Builder(EmailsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Eliminar");
                builder.setMessage("Desea eliminar el Email?");
                builder.setPositiveButton("Confirmar",
                        (dialog, which) -> {
                            mDatabaseReference.child(mMails.get(viewHolder.getAdapterPosition()).getKey()).setValue(null);
                            mAdapterMail.deleteItem(viewHolder.getAdapterPosition());
                            mAdapterMail.notifyDataSetChanged();
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                }).create().show();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


}