package com.example.mycalendar;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.Calendar;
import java.util.Locale;

//Η κλάση για το του προϊόν
//Περιλαμβάνει δύο κατασκευαστές και getters-setters για τα αντίστοιχα πεδία.

public class Event {
    private int id;
    private String eventName;
    private String eventDescription;
    private String fulfillmentDate;
    private String creationDate;
    private boolean isChecked; //Πεδίο για τον έλεγχο του checkbox.

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Event(String eventName, String eventDescription, String fulfillmentDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.fulfillmentDate = fulfillmentDate;
        this.creationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(Calendar.getInstance().getTime());
        this.isChecked  = false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Event(int id, String eventName, String eventDescription, String fulfillmentDate) {
        this.id = id;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.fulfillmentDate = fulfillmentDate;
        this.creationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        this.isChecked = false;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public String getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(String fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }
    public String getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                ", fulfillmentDate=" + fulfillmentDate +
                '}';
    }
}

