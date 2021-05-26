package com.example.adminappcall;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class AddMedicina extends AppCompatActivity {

    EditText mNombreEditText;
    EditText mNumeroEditText;
    ImageView mFotoImageView;
    Uri finalurl, url = Uri.parse("0");
    Button mConfirmButton;
    FirebaseUser user;
    TextView chooseTime,selectDate;
    String lista;
    private StorageReference mStorage;
    private static final int CAMARA=1;
    private DatabaseReference mDatabaseReference;
    TimePickerDialog timePickerDialog;
    Calendar calendar, horaini,horafin;
    int currentHour,currentMinute, year,month,dayOfMonth,year2,month2,dayOfMonth2;
    int diff;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_medicina);
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        horaini = Calendar.getInstance();
        horafin = Calendar.getInstance();
        user = mAuth.getCurrentUser();
        mNombreEditText = findViewById(R.id.nameEditText);
        mNumeroEditText = findViewById(R.id.NumeroEditText);
        mFotoImageView = findViewById(R.id.fotoImageView);
        mConfirmButton = findViewById(R.id.ConfirmarButton);
        String mSubuser= Objects.requireNonNull(getIntent().getExtras()).getString("subuser");


        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+mSubuser);

        mDatabaseReference.child("mail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista = Objects.requireNonNull(dataSnapshot.getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+mSubuser+"/medicinas");

        mDatabaseReference =   mDatabaseReference.child(Objects.requireNonNull(mDatabaseReference.push().getKey()));
        mStorage = FirebaseStorage.getInstance().getReference(user.getUid());
        mFotoImageView.setOnClickListener(v -> {

            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                        },
                        CAMARA);
            }else{
                openCamera();

            }
          });
        mConfirmButton.setOnClickListener(v -> {
            if (!mNombreEditText.getText().toString().isEmpty() && !mNumeroEditText.getText().toString().isEmpty()  && !chooseTime.getText().toString().isEmpty() && !selectDate.getText().toString().isEmpty() ){
                String name = mNombreEditText.getText().toString();
                String numero = mNumeroEditText.getText().toString();
                mDatabaseReference.child("horario").setValue(chooseTime.getText().toString());
                mDatabaseReference.child("nombre").setValue(name);
                mDatabaseReference.child("dosis").setValue(numero);
                mDatabaseReference.child("dia").setValue(dayOfMonth2);
                mDatabaseReference.child("año").setValue(year2);
                mDatabaseReference.child("mes").setValue(month2+1);
                mDatabaseReference.child("url").setValue(String.valueOf(finalurl));
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE,"Medicina");
                intent.putExtra(CalendarContract.Events.DESCRIPTION,mNombreEditText.getText().toString());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,horaini.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,horafin.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.HAS_ALARM,true);
                intent.putExtra(CalendarContract.Events.RRULE,"FREQ=DAILY;COUNT="+diff+";");
                intent.putExtra(Intent.EXTRA_EMAIL,lista);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Por favor rellena todas los parametros", Toast.LENGTH_LONG).show();
            }

        });

        chooseTime = findViewById(R.id.btnDate2);
        chooseTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(this, (timePicker, hourOfDay, minutes) -> {
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                horaini.set(Calendar.HOUR_OF_DAY,hourOfDay);
                horaini.set(Calendar.MINUTE,minutes);
                horafin.set(Calendar.HOUR_OF_DAY,hourOfDay);
                horafin.set(Calendar.MINUTE,minutes+15);
            }, currentHour, currentMinute, true);

            timePickerDialog.show();
        });
        selectDate = findViewById(R.id.btnDate);
        selectDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                dayOfMonth2=dayOfMonth;
                year2=year;
                month2=month;
                horafin.set(Calendar.MONTH,month2);
                horafin.set(Calendar.YEAR,year2);
                horafin.set(Calendar.DAY_OF_MONTH,dayOfMonth2);
                diff = (int)TimeUnit.DAYS.convert( horafin.getTimeInMillis() - horaini.getTimeInMillis(), TimeUnit.MILLISECONDS);
            });
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
            horafin.set(Calendar.MONTH,month);
            horafin.set(Calendar.YEAR,year);
            horafin.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMARA && resultCode == RESULT_OK){
            mFotoImageView.setImageURI(url);
            StorageReference    filepath= mStorage.child(String.valueOf(url.getLastPathSegment()));
            filepath.putFile(url).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri ->finalurl =uri));
        }
    }
    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From camera");
        url = getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, url);
        startActivityForResult(intent, CAMARA);
    }

}