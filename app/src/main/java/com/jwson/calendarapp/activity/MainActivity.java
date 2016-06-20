package com.jwson.calendarapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;

import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.SwipePageAdapter;
import com.jwson.calendarapp.couchbase.CouchbaseHelper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";

    Database database = null;

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloCBL();

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new SwipePageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(1);
    }


    private void helloCBL() {
        try {
            Manager manager = CouchbaseHelper.getInstance().getManager(getApplicationContext());
            database = CouchbaseHelper.getInstance().getDatabase(manager, DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return;
        }
        // Create the document
        String documentId = createDocument(database);
    /* Get and output the contents */
        // retrieve the document from the database
        Document retrievedDocument = database.getDocument(documentId);
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    /* Update the document and add an attachment */
        updateDoc(database, documentId);
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
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

    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Big Party");
        map.put("location", "My House");
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            moveTaskToBack(true);
            MainActivity.this.finish();
        } else {
            getFragmentManager().popBackStack();
        }

    }
}


