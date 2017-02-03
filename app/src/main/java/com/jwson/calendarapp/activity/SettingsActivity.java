package com.jwson.calendarapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.UserEvents;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    private ListView lv;

    private static final String SHARE_WITH_FRIENDS = "Share with Friends";
    private static final String NOTIFICATIONS = "Notifications";
    private static final String CONTROLS = "CONTROLS";
    private static final String SUPPORT = "Support";
    private static final String MY_ACCOUNT = "My Account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lv = (ListView) findViewById(R.id.settings_options);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        final List<String> setting_list = new ArrayList<String>();
        setting_list.add(SHARE_WITH_FRIENDS);
        setting_list.add(NOTIFICATIONS);
        setting_list.add(CONTROLS);
        setting_list.add(SUPPORT);
        setting_list.add(MY_ACCOUNT);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                setting_list );

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = setting_list.get(position);
                if(name.equals(MY_ACCOUNT)){
                    startActivity(new Intent(SettingsActivity.this, MyAccountActivity.class));
                }
            }
        });
    }


}
