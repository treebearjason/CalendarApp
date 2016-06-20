package com.jwson.calendarapp.fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jwson.calendarapp.R;
import com.jwson.calendarapp.activity.CreateNewEventActivity;
import com.jwson.calendarapp.activity.EventActivity;
import com.jwson.calendarapp.domain.UserEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TimelineFragment extends Fragment implements View.OnClickListener {

    private List<UserEvents> myEvents = new ArrayList<UserEvents>();
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd @HH:mm");

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            UserEvents newEvent = (UserEvents) extras.get("com.jwson.calendarapp.domain.UserEvents");
            Log.i("INFO", "new event are created");
            myEvents.add(newEvent);
        }


        populateEventList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        Button createButton = (Button) view.findViewById(R.id.create_event);
        createButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateListView();

    }

    private void populateListView() {
        ArrayAdapter<UserEvents> adapter = new MyEventAdapter();
        ListView list = (ListView) getView().findViewById(R.id.eventListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserEvents event = myEvents.get(position);
                Intent intent = new Intent(getActivity(), EventActivity.class);
                intent.putExtra("com.jwson.calendarapp.domain.UserEvents", event);
                startActivity(intent);
            }
        });
    }

    private class MyEventAdapter extends ArrayAdapter<UserEvents> {
        public MyEventAdapter() {
            super(getActivity(), R.layout.item_timeline, myEvents);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_timeline, parent, false);
            }
            //Find the event to work with
            UserEvents event = myEvents.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.calendar_icon);
            imageView.setImageResource(event.getIconId());

            TextView eventNameText = (TextView) itemView.findViewById(R.id.eventName);
            eventNameText.setText(event.getName());

            TextView eventLocation = (TextView) itemView.findViewById(R.id.eventLocation);
            eventLocation.setText(event.getLocationName());

            TextView dateText = (TextView) itemView.findViewById(R.id.eventDate);

//            String dateStr = df.format(event.getDate());
//            dateText.setText(dateStr);

            return itemView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_event:
                Intent intent = new Intent(getActivity(), CreateNewEventActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void populateEventList() {
//        myEvents.add(new UserEvents(new Date(), "Basketball", "Victoria Park", R.drawable.day0));
//        myEvents.add(new UserEvents(new Date(), "Table Tennis", "Sai Wan Ho", R.drawable.day0));
//        myEvents.add(new UserEvents(new Date(), "Yum Cha", "利苑", R.drawable.day0));
//        myEvents.add(new UserEvents(new Date(), "Chill drinks", "LKF", R.drawable.day0));
//        myEvents.add(new UserEvents(new Date(), "Project Meeting", "Starbucks Coffee", R.drawable.day0));
//        myEvents.add(new UserEvents(new Date(), "Football", "Chai Wan", R.drawable.day0));

    }
}
