package com.jwson.calendarapp.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.FriendAdapter;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.domain.UserEvents;
import com.jwson.calendarapp.utils.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ShareEventActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShareEventActivity.class.getSimpleName();
    private FriendAdapter mFriendAdapter;
    private ListView mListView;
    private Firebase mActiveListRef, mSharedWithRef;
    private ValueEventListener mActiveListRefListener, mSharedWithListener;
    private Set<String> sharedEmailSet;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedEmailSet = new HashSet<String>();
        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Create Firebase references
         */


        Firebase currentUserFriendsRef = new Firebase(Constants.FIREBASE_URL_USER_FRIENDS).child(userId);

        /**
         * Set interactive bits, such as click events/adapters
         */
        mFriendAdapter = new FriendAdapter(ShareEventActivity.this, User.class,
                R.layout.single_user_item, currentUserFriendsRef, null);

        /* Set adapter for the listView */
        mListView.setAdapter(mFriendAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    /**
     * Cleanup the adapter when activity is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        /* Set adapter for the listView */
        mFriendAdapter.cleanup();
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListView = (ListView) findViewById(R.id.list_view_friends_share);
    }

    /**
     * Launch AddFriendActivity to find and add user to current user's friends list
     * when the button AddFriend is pressed
     */
    public void onAddFriendPressed(View view) {
        Intent intent = new Intent(ShareEventActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }

    public void onSelectFriend(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean checked = checkBox.isChecked();
        if(checked){
            sharedEmailSet.add(checkBox.getText().toString());
        }else {
            sharedEmailSet.remove(checkBox.getText().toString());
        }
    }

    public void onConfirmButtonClicked(View view) {
        Intent intent = new Intent(ShareEventActivity.this, CreateNewEventActivity.class);
        Log.v(LOG_TAG, new Gson().toJson(sharedEmailSet));
        intent.putExtra("friendList", sharedEmailSet.toArray(new String[0]));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }
}
