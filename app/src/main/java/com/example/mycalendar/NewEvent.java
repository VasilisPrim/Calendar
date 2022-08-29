package com.example.mycalendar;

import static java.lang.Integer.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Date;
import java.text.BreakIterator;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Locale;

//Κλάση για τη δημιουργία καινούργιου event.
@RequiresApi(api = Build.VERSION_CODES.N)
public class NewEvent extends AppCompatActivity  {
    //Δήλωση μεταβλητών
    EditText event_title,description,date;
    Button store_btn,delete_btn,return_btn;
    private Event newEvent,updateEvent;
    private  MyDatabase myDatabase = new MyDatabase(NewEvent.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        event_title = findViewById(R.id.event_title);
        description = findViewById(R.id.description);
        store_btn = findViewById(R.id.save_btn);
        delete_btn = findViewById(R.id.deleteNew_btn);
        return_btn = findViewById(R.id.return_btn);
        date = findViewById(R.id.date);
        //Intent για να παίρνουμε δεδομένα από την mainActivity
        //Συγκεκριμένα θα παίρνουμε ένα πίνακα με strings με δεδομένα για τα πεδία edittext στη περίπτωση που θέλουμε να τα κάνουμε update
        //και στη περίπτωση που θέλουμε να δημιουργήσουμε καινούριο event(πίνακας με άδεια πεδία).
        Intent intent = getIntent();
        String[] str = intent.getStringArrayExtra("message");
        event_title.setText(str[0]);
        description.setText(str[1]);
        date.setText(str[2]);
        String isForUpdate  = str[3];
        int itemId = Integer.parseInt(str[4]);
        int itemPosition = Integer.parseInt(str[5]);

        store_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //H περίπτωση καινούργιου event
                if(isForUpdate.equals("No"))
                {
                    if(isValidDate(date.getText().toString())) //έλεγχος έγκυρης ημερομηνίας
                    {
                    newEvent = new Event(event_title.getText().toString(),description.getText().toString(), date.getText().toString());
                    myDatabase.addOne(newEvent); //Προσθήκη καινούργιου event
                    //Intent για να επιστρέφουμε στην αρχική activity με το που προστεθεί γεγονός στη βάση δεδομένων.
                    String  value = "ok";
                    Intent startNewEvent = new Intent();
                    startNewEvent.putExtra("result",value);
                    setResult(78,startNewEvent);
                    finish();
                    }
                    else{
                        Toast.makeText(NewEvent.this, "You have to give a date in the format of dd/MM/yyyy hh:mm", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                //Η περίπτωση του update
                {
                    updateEvent = new Event(event_title.getText().toString(),description.getText().toString(), date.getText().toString());
                    myDatabase.onUpdate(updateEvent,itemId); //Περνάμε το ανανεωμένο γεγονός μαζί με την ταυτότητά του.
                    String  value = "ok";
                    Intent startNewEvent = new Intent();
                    startNewEvent.putExtra("result",value);
                    setResult(78,startNewEvent);
                    finish();
                }
            }
        }
        );
//        date.setOnClickListener(new View.OnClickListener() {
//            //Μεθοδος για την επιλογή ημερομηνίας από πίνακα με ημερομηνίες.
//            @Override
//            public void onClick(View view) {
//                final Calendar calendar = Calendar.getInstance ();
//                int mYear = calendar.get(Calendar.YEAR);
//                int mMonth = calendar.get(Calendar.MONTH);
//                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog ( NewEvent.this, new DatePickerDialog.OnDateSetListener () {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        date.setText ( dayOfMonth + "/" + (month + 1) + "/" + year );
//                    }
//                }, mYear, mMonth, mDay );
//                datePickerDialog.show ();
//            }
//        });
        return_btn.setOnClickListener(new View.OnClickListener() {
            //Το κουμπί της επιστροφής
            @Override
            public void onClick(View view) {
                String  value = "ok";
                Intent startNewEvent = new Intent();
                startNewEvent.putExtra("result",value);
                setResult(78,startNewEvent);
                finish();
            }
        });
        if(isForUpdate.equals("No"))
        {
            //Σε περίπτωση νέου γεγονότος απενεργοποιούμε το κουμπί της διαγραφής
            delete_btn.setEnabled(false);
        }
        else{
            //Αλλιώς μπορούμε να διαγράψουμε το γεγονός και να επιστρέψουμε (με το Intent) στην mainActivity
            delete_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                myDatabase.deleteOne(myDatabase.getTheEvents().get(itemPosition));
                String  value = "ok";
                Intent startNewEvent = new Intent();
                startNewEvent.putExtra("result",value);
                setResult(78,startNewEvent);
                finish();
            }
        });
    }
    }
    //Η συνάρτηση για τον έλεγχο της ημερομηνίας.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  boolean isValidDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        try{
            sdf.parse(date);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
