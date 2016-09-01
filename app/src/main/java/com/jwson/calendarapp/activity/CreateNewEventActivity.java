package com.jwson.calendarapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

//import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
//import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.common.collect.Sets;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.UserEvents;
import com.jwson.calendarapp.utils.Constants;
import com.jwson.calendarapp.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateNewEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText startDateText;
    private EditText endDateText;
    private String userId;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Firebase.setAndroidContext(this);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        startDateText = (EditText) findViewById(R.id.create_start_date);
        startDateText.setInputType(InputType.TYPE_NULL);

        endDateText = (EditText) findViewById(R.id.create_end_date);
        endDateText.setInputType(InputType.TYPE_NULL);

        setDateTimeField();
    }

    private void setDateTimeField() {
        startDateText.setOnClickListener(this);
        endDateText.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        Date defaultDate = DateUtils.clearTime(new Date());
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date) {
                        if (view instanceof EditText) {
                            EditText editText = (EditText) view;
                            editText.setText(mFormatter.format(date));
                        }
                    }
                })
                .setInitialDate(defaultDate)
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                .setIs24HourTime(true)
                .setTheme(SlideDateTimePicker.HOLO_DARK)
                .setIndicatorColor(Color.parseColor("#FF8C00"))
                .build()
                .show();
    }

    public void confirmButtonClicked(View view) throws ParseException {
        EditText eventName = (EditText) findViewById(R.id.create_new_name);

        EditText startDateText = (EditText) findViewById(R.id.create_start_date);
        EditText endDateText = (EditText) findViewById(R.id.create_end_date);

        EditText location = (EditText) findViewById(R.id.create_new_location);

        String message = eventName.getText().toString() + ", " + startDateText.getText().toString() + ", " + location.getText().toString();
        Log.v("Event created", message);

        String eventNameStr = eventName.getText().toString();
        String startDateStr = startDateText.getText().toString();
        String endDateStr = endDateText.getText().toString();
        String locationStr = location.getText().toString();

        Date startDate = mFormatter.parse(startDateStr);
        Date endDate = mFormatter.parse(endDateStr);

        UserEvents newEvent = new UserEvents();
        newEvent.setStartDate(startDate);
        newEvent.setEndDate(endDate);
        newEvent.setCreateDate(new Date());
        newEvent.setLocationName(locationStr);
        newEvent.setName(eventNameStr);
        newEvent.setIconId(R.drawable.day0);
        newEvent.setAdmins(Sets.newHashSet(userId));

        String docId = UUID.randomUUID().toString();
        newEvent.setId(docId);

        writeEventToDB(newEvent);

        Intent intent = new Intent(CreateNewEventActivity.this, MainActivity.class);
//        intent.putExtra("com.jwson.calendarapp.domain.UserEvents", documentId);
        startActivity(intent);
        finish();
    }

    private void writeEventToDB(UserEvents event) {
        // Create a new document and add data
        Firebase ref = new Firebase(Constants.FIREBASE_URL);

        Map<String, Object> eventsToUpdate = new HashMap<String, Object>();
        eventsToUpdate.put("/eventList/" + event.getId(), new ObjectMapper().convertValue(event, Map.class));
        eventsToUpdate.put("/userEvents/" + userId, new ObjectMapper().convertValue(event, Map.class));
        eventsToUpdate.put("/pendingEvents/" + userId, new ObjectMapper().convertValue(event, Map.class));

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference eventListRef = database.getReference("eventList");
//        DatabaseReference userEventsRef = database.getReference("userEvents");
//        DatabaseReference pendingEventsRef = database.getReference("pendingEvents");

        ref.updateChildren(eventsToUpdate);

    }
}
