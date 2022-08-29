package com.example.mycalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView events ;
    Button deleteButton,new_event;
    MyDatabase myDatabase ;
    private List<Event> allEvents;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            //Η μέθοδος που λαμβάνει τα αποτελέσματα από την δεύτερη activity
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 78){
                        Intent intent = result.getData();
                        if (intent != null){
                            extractEvents();
                        }
                    }
                }});

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        events = findViewById(R.id.event_list);
        deleteButton = findViewById(R.id.delete_btn);
        new_event = findViewById(R.id.new_event_btn);
        myDatabase = new MyDatabase(MainActivity.this);

        extractEvents();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvents();
            }
        });
        new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //H περίπτωση που έχουμε νέο γεγονός
                Intent startNewEvent = new Intent(MainActivity.this,NewEvent.class);
                String[] strArray =  {"", "", "","No","0","0"}; //Ο πίνακας που θα στείλουμε στην άλλη activity με τα κενά πεδία αφού έχουμε νέο γεγονός
                startNewEvent.putExtra("message",strArray);
                activityLauncher.launch(startNewEvent);
            }
        });

        events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Η περίπτωση που επιλέγουμε ένα γεγονός απο τη λίστα και θέλουμε να δούμε τα πεδία στην άλλη activity και πιθανώς να τα ανανεώσουμε.Γιαυτό στέλνουμε πίνακα με τα πεδία του γεγονότος.
                Event event = (Event) adapterView.getItemAtPosition(i);
                String[] strArray =  {event.getEventName(), event.getEventDescription(), event.getFulfillmentDate(),"Yes",String.valueOf(event.getId()),String.valueOf(i)};
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                intent.putExtra("message",strArray);
                activityLauncher.launch(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deleteEvents(){
        //Διαγραφή γεγονότων όπου το κουμπί δίπλα στα γεγονότα είναι τσεκαρισμένο.
        for(int i=0;i < allEvents.size();i++){
            if(allEvents.get(i).isChecked() == true){
                myDatabase.deleteOne(allEvents.get(i));
            }
        }
        extractEvents();
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void extractEvents(){
        //Η συνάρτηση απο την οποία πέρνουμε τα γεγονότα απο τη βάση δεδομένων,τα βάζουμε στο listview με τον adapter και ελέγχουμε επίσης και αν η ημ/νια έχει παρέλθει.
        myDatabase = new MyDatabase(MainActivity.this);
        allEvents = myDatabase.getTheEvents();
        Calendar cal = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

        for(int i=0;i < allEvents.size();i++){
            //Έλεγχος για την  ημερομηνία
           try {
                cal.setTime(sdf.parse(allEvents.get(i).getFulfillmentDate()));
                calNow.setTime(Calendar.getInstance().getTime());
                int i1 = cal.compareTo(calNow);
                if (i1<0){
                    myDatabase.deleteOne(allEvents.get(i));
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Give a valid date format like - dd/MM/yyyy HH:mm", Toast.LENGTH_SHORT).show();
            }
        }
        allEvents = myDatabase.getTheEvents();//Παίρνουμε τη λίστα δεδομένων χρησιμοποιώντας τη συνάρτηση της βάσης δεδομένων.
        Collections.sort(allEvents,new CustomComparator()); // Ταξινόμηση της λίστας με βάση την ημ/νια χρησιμοποιώντας custom comparator
        EventsAdapter<Event> eventAdapter =  new EventsAdapter<Event>(MainActivity.this,R.layout.event_list_item, (ArrayList<Event>) allEvents);//O custom adapter
        events.setAdapter(eventAdapter);
    }
}


