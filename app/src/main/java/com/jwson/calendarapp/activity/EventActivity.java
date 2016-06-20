package com.jwson.calendarapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.UserEvents;

import java.text.SimpleDateFormat;

public class EventActivity extends AppCompatActivity {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd @HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        UserEvents event = (UserEvents)getIntent().getSerializableExtra("com.jwson.calendarapp.domain.UserEvents");

        TextView eventDate = (TextView) findViewById(R.id.detail_event_date);
//        String dateStr = df.format(event.getDate());
//        eventDate.setText(dateStr);

        TextView eventName = (TextView)findViewById(R.id.detail_event_name);
        eventName.setText(event.getName());

        TextView eventLocation = (TextView)findViewById(R.id.detail_event_location);
//        eventLocation.setText(event.getLocation());


    }




}
