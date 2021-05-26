package com.example.adminappcall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AddSubuser extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private static final int GALLERY_INTENT=1;
    FirebaseAuth mAuth;
    String a,b,c,id;
    Uri url = Uri.parse("0");
    private StorageReference mStorage;
    ImageView mFotoImageView;
    EditText nombre,edad,genero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subuser);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        id =  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mStorage = FirebaseStorage.getInstance().getReference(id);

        nombre = findViewById(R.id.nameEditText);
        edad = findViewById(R.id.edad);
        genero = findViewById(R.id.sexo);
        mFotoImageView= findViewById(R.id.fotoImageView);
        mFotoImageView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,GALLERY_INTENT);
        });

        findViewById(R.id.ConfirmarButton).setOnClickListener(view -> signIn());
        createRequest();
    }


    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }


    private void signIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        a =  nombre.getText().toString();
        b =  edad.getText().toString();
        c =  genero.getText().toString();

        if (!String.valueOf(url).equals("0")){
            StorageReference filepath= mStorage.child(url.getLastPathSegment());
            filepath.putFile(url).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> url=uri));
        }

        if (a.equals("")){
            Toast.makeText( this, "Debe introducir un nombre.", Toast.LENGTH_SHORT).show();
        }else{
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if (requestCode == GALLERY_INTENT) {
            if (resultCode == RESULT_OK){
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


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("users/"+id+ "/subusers/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        mDatabase.setValue(a);
                        mDatabase2.child("edad").setValue(b);
                        mDatabase2.child("nombre").setValue(a);
                        mDatabase2.child("genero").setValue(c);
                        mDatabase2.child("ciego").setValue("no");
                        mDatabase2.child("mail").setValue(mAuth.getCurrentUser().getEmail());
                        if (!String.valueOf(url).equals("0")){
                            StorageReference filepath= mStorage.child(url.getLastPathSegment());
                            filepath.putFile(url).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> mDatabase2.child("foto").setValue(uri.toString())));
                        }else{
                            mDatabase2.child("foto").setValue("0");

                        }


                        mGoogleSignInClient.signOut();
                        mAuth.signOut();
                        // Sign in success, update UI with the signed-in user's information
                        Intent intent = new Intent(this,LogInActivity.class);
                        intent.putExtra("user", "1");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText( this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
