package com.example.mycalendar;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

// Ο comparator για σύγκριση ημερομηνιών.
public class CustomComparator implements Comparator<Event> {
    MainActivity activity  = new MainActivity();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int compare(Event t0, Event t1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Date d1 = null;
        Date d2 = null;
        try{
            d1 = sdf.parse(t0.getFulfillmentDate());
            d2= sdf.parse(t1.getFulfillmentDate());
            cal.setTime(d1);
            cal1.setTime(d2);
        } catch(Exception e){
            e.printStackTrace();
        }
        return cal.compareTo(cal1);
}}
