package com.jwson.calendarapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
//import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
//import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.couchbase.CouchbaseHelper;
import com.jwson.calendarapp.domain.UserEvents;
import com.jwson.common.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateNewEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText startDateText;
    private EditText endDateText;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";
    Database database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        startDateText = (EditText) findViewById(R.id.create_start_date);
        startDateText.setInputType(InputType.TYPE_NULL);

        endDateText = (EditText) findViewById(R.id.create_end_date);
        endDateText.setInputType(InputType.TYPE_NULL);

        setDateTimeField();
        initializeDB();
    }

    private void setDateTimeField() {
        startDateText.setOnClickListener(this);
        endDateText.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        Date defaultDate = DateUtils.clearTime(new Date());
//        new SlideDateTimePicker.Builder(getSupportFragmentManager())
//                .setListener(new SlideDateTimeListener() {
//                    @Override
//                    public void onDateTimeSet(Date date) {
//                        if (view instanceof EditText) {
//                            EditText editText = (EditText) view;
//                            editText.setText(mFormatter.format(date));
//                        }
//                    }
//                })
//                .setInitialDate(defaultDate)
//                        //.setMinDate(minDate)
//                        //.setMaxDate(maxDate)
//                .setIs24HourTime(true)
//                .setTheme(SlideDateTimePicker.HOLO_DARK)
//                .setIndicatorColor(Color.parseColor("#FF8C00"))
//                .build()
//                .show();
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

//        Date startDate = mFormatter.parse(startDateStr);
//        Date endDate = mFormatter.parse(endDateStr);

        UserEvents newEvent = new UserEvents();
//        newEvent.setStartDate(startDate);
//        newEvent.setEndDate(endDate);
        newEvent.setLocationName(locationStr);
        newEvent.setName(eventNameStr);
        newEvent.setIconId(R.drawable.day0);
        String docId = UUID.randomUUID().toString();
        newEvent.setId(docId);

        writeEventToDB(newEvent);

//        Document retrievedDocument = database.getDocument(documentId);
//        Log.d(TAG, "retrievedDocument=" + retrievedDocument.getProperties().toString());

        Intent intent = new Intent(CreateNewEventActivity.this, MainActivity.class);
//        intent.putExtra("com.jwson.calendarapp.domain.UserEvents", documentId);
        startActivity(intent);
        finish();
    }

    private void writeEventToDB(UserEvents event) {
        // Create a new document and add data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("events");

        myRef.child(event.getId()).setValue(event);

//        Gson gson = new Gson();
//        Document document = database.getDocument(event.getId());
//        String documentId = document.getId();
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        map.put("result", gson.toJson(event));
//        try {
//            // Save the properties to the document
//            document.putProperties(map);
//        } catch (CouchbaseLiteException e) {
//            Log.e(TAG, "Error putting", e);
//        }
//        return documentId;
    }

    private void initializeDB() {
        try {
            Manager manager = CouchbaseHelper.getInstance().getManager(getApplicationContext());
            database = CouchbaseHelper.getInstance().getDatabase(manager, DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return;
        }
    }
}
