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
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.Revision;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.activity.CreateNewEventActivity;
import com.jwson.calendarapp.activity.EventActivity;
import com.jwson.calendarapp.couchbase.CouchbaseHelper;
import com.jwson.calendarapp.domain.UserEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class TimelineFragment extends Fragment implements View.OnClickListener {
    ArrayAdapter<UserEvents> adapter;
    private List<UserEvents> myEvents = new ArrayList<UserEvents>();
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd @HH:mm");

    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";

    Database database = null;

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (database == null) {
            try {
                Manager manager = CouchbaseHelper.getInstance().getManager(getActivity().getApplicationContext());
                database = CouchbaseHelper.getInstance().getDatabase(manager, DB_NAME);
            } catch (Exception e) {
                Log.e(TAG, "Error getting database", e);
                return;
            }
        }

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String documentId = (String) extras.get("com.jwson.calendarapp.domain.UserEvents");
            Log.i("INFO", "new event are created: " + documentId);
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
        adapter = new MyEventAdapter();
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

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UserEvents event = myEvents.get(position);
                try {
                    Document doc2Delete = database.getDocument(event.getId());
                    doc2Delete.delete();
                    myEvents.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity().getApplicationContext(), event.getName() + " has been deleted!", Toast.LENGTH_SHORT).show();
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                return false;
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

            String dateStr = df.format(event.getStartDate());
            dateText.setText(dateStr);

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
        Query queryAllDocs = database.createAllDocumentsQuery();
        QueryEnumerator queryEnumerator = null;
        Gson gson = new Gson();
        try {
            queryEnumerator = queryAllDocs.run();

            for (Iterator<QueryRow> it = queryEnumerator; it.hasNext(); ) {
                QueryRow row = it.next();
                Document document = row.getDocument();
                Log.d(TAG, "document: " + document.getProperties().toString());

                String docObj = (String) document.getProperties().get("result");

                myEvents.add(gson.fromJson(docObj, UserEvents.class));
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "done looping over all docs ");
    }

    private void helloCBL() {
        // Create the document
        // String documentId = createDocument(database);
    /* Get and output the contents */
        // retrieve the document from the database
//        Document retrievedDocument = database.getDocument(documentId);
//        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    /* Update the document and add an attachment */
//        updateDoc(database, documentId);
//        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
        // Add an attachment
//        addAttachment(database, documentId);
    /* Get and output the contents with the attachment */
//        outputContentsWithAttachment(database, documentId);
    }

    private void updateDoc(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put("eventDescription", "Everyone is invited!");
            updatedProperties.put("address", "123 Elm St.");
            // Save to the Couchbase local Couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }
}
