package com.example.mycalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
//Η κλάση για τη δημιουργία του πίνακα της βάσης δεδομένων με συναρτήσεις για ανανέωση,διαγραφή,προσθήκη και επιστροφή λίστας με γεγονότα.
public class MyDatabase  extends SQLiteOpenHelper {

    private static final String EVENTS_TABLE ="EVENTS_TABLE" ;
    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String EVENT_DESC = "EVENT_DESC";
    public static final String DATE = "DATE";
    public static final String ID = "ID";
    public  static final String CREATION_DATE  = "CREATION_DATE";
    public MyDatabase(@Nullable Context context) {
        super(context,"my_events.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + EVENTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME + " TEXT, " + EVENT_DESC +" TEXT, "+ DATE +" TEXT, "+ CREATION_DATE +" TEXT "+")";
        db.execSQL(createTable);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    //Προσθήκη γεγονότος
    public boolean addOne(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EVENT_NAME,event.getEventName());
        cv.put(EVENT_DESC,event.getEventDescription());
        cv.put(DATE,event.getFulfillmentDate());
        cv.put(CREATION_DATE,event.getCreationDate());

        long insert = db.insert(EVENTS_TABLE, null, cv);

        if (insert == -1) return  false;
        else return true;
    }
    public String findCreationDate(Event event){
        //Συνάρτηση που επιστρέφει την ημερομηνία δημιουργίας του γεγονότος
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+CREATION_DATE+ " FROM "+EVENTS_TABLE+" WHERE "+ID+" = "+event.getId();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return cursor.getString(0);
        else return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    //Επιστροφή λίστας με τα γεγονότα,απο τον σχεσιακό πίνακα.
    public List<Event> getTheEvents(){
        List<Event> eventList = new ArrayList<>();
        String query = "SELECT * FROM "+EVENTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                int eventId = cursor.getInt(0);
                String eventName = cursor.getString(1);
                String eventDescription = cursor.getString(2);
                String eventDate = cursor.getString(3);
                Event newEvent = new Event(eventId,eventName,eventDescription,eventDate);
                eventList.add(newEvent);
            }while (cursor.moveToNext());
        }
        else{}
        cursor.close();
        db.close();
        return eventList;
    }
    public boolean deleteOne(Event event){
        //Διαγραφή γεγονότος
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ EVENTS_TABLE+" WHERE "+ID+" = "+event.getId();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return true;
        else return false;
    }
    public boolean onUpdate(Event event,int id){
        //Ανανέωση γεγονότος
        SQLiteDatabase db = this.getWritableDatabase();
        String query  = "UPDATE "+ EVENTS_TABLE+" SET "+EVENT_NAME+ " = "+ "'"+event.getEventName()+"'"+", "+
                EVENT_DESC+ " = "+ "'"+event.getEventDescription()+"'"+", "+DATE+ " = "+ "'"+event.getFulfillmentDate()+"'"+
                ", "+CREATION_DATE+ " = "+ "'"+event.getCreationDate()+"'"+
                " WHERE "+ID+" = "+id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) return true;
        else return false;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
