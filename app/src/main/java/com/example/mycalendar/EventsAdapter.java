package com.example.mycalendar;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

//Η κλάση του custom adapter



@RequiresApi(api = Build.VERSION_CODES.O)
public class EventsAdapter<E> extends ArrayAdapter<Event> {
    private final ArrayList<Event> events;
    private final Context context;
    public EventsAdapter(@NonNull Context context, int textViewResourceId, ArrayList<Event> events) {
        super(context,textViewResourceId,events);
        this.events = new ArrayList<Event>();
        this.events.addAll(events);
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    @Override
  public int getCount() {
       return events.size();
    }

  @Override
    public Event getItem(int i) {
       return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        final Event event = events.get(position);
        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.event_list_item, null);


            final TextView nameTextView = view.findViewById(R.id.eventName);
            final TextView creationDateTextView = view.findViewById(R.id.creationDate);
            final TextView fulfillDateTextView = view.findViewById(R.id.fulfillmentDate);
            final CheckBox  eventCheckbox = view.findViewById(R.id.checkEvent);

            ViewHolder viewHolder = new ViewHolder(nameTextView,creationDateTextView,fulfillDateTextView,eventCheckbox);
            view.setTag(viewHolder);
        }
        final ViewHolder viewHolder = (ViewHolder)view.getTag();
        viewHolder.eventCheckbox.setOnClickListener(new View.OnClickListener() {
            //Βάζουμε Listener για το checkbox και δίνουμε την αντίστοιχη τιμή στο πεδίο isChecked της κλάσης Event.
            @Override
            public void onClick(View view) {
                if(viewHolder.eventCheckbox.isChecked()){
                    event.setChecked(true);}
                else event.setChecked(false);
            }
        });
        MyDatabase myDatabase = new MyDatabase(getContext());
        viewHolder.nameTextView.setText(event.getEventName());
        viewHolder.creationDateTextView.setText("This event note created at: "+myDatabase.findCreationDate(event));
        viewHolder.fulfillDateTextView.setText("This event is scheduled for: "+event.getFulfillmentDate());
        return view;

    }

    private class ViewHolder {
        private final TextView nameTextView;
        private final TextView creationDateTextView;
        private final TextView fulfillDateTextView;
        private final CheckBox eventCheckbox;

        public ViewHolder(TextView nameTextView, TextView creationDateTextView, TextView fulfillDateTextView, CheckBox eventCheckbox) {
            this.nameTextView = nameTextView;
            this.creationDateTextView = creationDateTextView;
            this.fulfillDateTextView = fulfillDateTextView;
            this.eventCheckbox = eventCheckbox;
        }
    }
}


