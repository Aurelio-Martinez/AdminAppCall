package com.example.adminappcall;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class MainActivityCalendar extends AppCompatActivity {




    EditText titulo,lugar,descripcion;
    TextView chooseTime,chooseTime2,selectDate;
    TimePickerDialog timePickerDialog;
    Calendar calendar, horaini, horafin;
    int currentHour,currentMinute;
    public DatabaseReference mDatabaseReference;
    DatePickerDialog datePickerDialog;
    int year,month,dayOfMonth;
    Button confirm;
    Intent origin;
    String lista;
    SwitchCompat fullDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        origin = getIntent();

        mDatabaseReference = FirebaseDatabase.getInstance("https://appcall-default-rtdb.europe-west1.firebasedatabase.app").getReference("subusers/"+origin.getStringExtra("key"));

        mDatabaseReference.child("mail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               lista = Objects.requireNonNull(dataSnapshot.getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabaseReference.child("mails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    lista += ", " + Objects.requireNonNull(postSnapshot.getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setContentView(R.layout.activity_main_calendar);
        horaini = Calendar.getInstance();
        horafin = Calendar.getInstance();
        titulo = findViewById(R.id.Titulo);
        lugar = findViewById(R.id.Lugar);
        descripcion = findViewById(R.id.Descripcion);
        selectDate = findViewById(R.id.btnDate);
        fullDay =findViewById(R.id.switch1);
        chooseTime2 = findViewById(R.id.etChooseTime2);
        chooseTime = findViewById(R.id.etChooseTime);


        fullDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (fullDay.isChecked()){
                chooseTime.setVisibility(View.INVISIBLE);
                chooseTime2.setVisibility(View.INVISIBLE);
            }else{
                chooseTime.setVisibility(View.VISIBLE);
                chooseTime2.setVisibility(View.VISIBLE);
            }
        });

        chooseTime.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(MainActivityCalendar.this, (timePicker, hourOfDay, minutes) -> {
                chooseTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                horaini.set(Calendar.HOUR_OF_DAY,hourOfDay);
                horaini.set(Calendar.MINUTE,minutes);
            }, currentHour, currentMinute, true);

            timePickerDialog.show();
        });

        chooseTime2.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(MainActivityCalendar.this, (timePicker, hourOfDay, minutes) -> {
                chooseTime2.setText(String.format("%02d:%02d", hourOfDay, minutes)  );
                horafin.set(Calendar.HOUR_OF_DAY,hourOfDay);
                horafin.set(Calendar.MINUTE,minutes);
            }, currentHour, currentMinute, true);

            timePickerDialog.show();
        });

        selectDate.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                horaini.set(Calendar.MONTH,month);
                horaini.set(Calendar.YEAR,year);
                horaini.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                horafin.set(Calendar.MONTH,month);
                horafin.set(Calendar.YEAR,year);
                horafin.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            });
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        confirm = findViewById(R.id.ConfirmarButton);
        confirm.setOnClickListener(v -> {
            if (!titulo.getText().toString().isEmpty() && !lugar.getText().toString().isEmpty() &&  !descripcion.getText().toString().isEmpty()){
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE,titulo.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION,descripcion.getText().toString());
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION,lugar.getText().toString());
                intent.putExtra(CalendarContract.Events.ALL_DAY,fullDay.isChecked());
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,horaini.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,horafin.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.HAS_ALARM,true);
                intent.putExtra(Intent.EXTRA_EMAIL,lista);
                startActivity(intent);

            }else{
                Toast.makeText(this, "Por favor rellena todas los parametros", Toast.LENGTH_LONG).show();
            }
        });
    }
}

