package com.jwson.calendarapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.EventFriend;
import com.jwson.calendarapp.domain.UserEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd @HH:mm");
    private DatabaseReference mDatabase;
    private ListView attendingListView;
    private ListView pendingListView;
    private UserEvents event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        event = (UserEvents)getIntent().getSerializableExtra("com.jwson.calendarapp.domain.UserEvents");

        TextView eventDate = (TextView) findViewById(R.id.detail_event_date);
        String dateStr = df.format(event.getStartDate());
        eventDate.setText(dateStr);

        TextView eventName = (TextView)findViewById(R.id.detail_event_name);
        eventName.setText(event.getName());

        TextView eventLocation = (TextView)findViewById(R.id.detail_event_location);
        eventLocation.setText(event.getLocationName());

        createEngagedUsersView();
    }

    private void createEngagedUsersView(){
        attendingListView = (ListView)findViewById(R.id.attending_users);
        List<EventFriend> attending_friend = event.getConfirmedFriends();

        if(attending_friend != null){
            List<String> aMobiles = new ArrayList<>();
            for(EventFriend fd : attending_friend){
                aMobiles.add(fd.getMobile());
            }
            ArrayAdapter<String> attending_adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    aMobiles );
            attendingListView.setAdapter(attending_adapter);
        }


        pendingListView = (ListView) findViewById(R.id.pending_users);
        List<EventFriend> pending_friends = event.getInvitedFriends();
        if(pending_friends!=null){
            List<String> pMobiles = new ArrayList<>();
            for(EventFriend fd : pending_friends){
                pMobiles.add(fd.getMobile());
            }
            ArrayAdapter<String> pending_adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    pMobiles );

            pendingListView.setAdapter(pending_adapter);
        }



    }



}
