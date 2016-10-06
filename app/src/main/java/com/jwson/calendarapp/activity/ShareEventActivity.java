package com.jwson.calendarapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.FriendAdapter;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShareEventActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShareEventActivity.class.getSimpleName();
    private FriendAdapter mFriendAdapter;
    private ListView mListView;
    private String userId;
    private List<User> userList = new ArrayList<User>();
    ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initializeScreen();
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {

        Firebase currentUserFriendsRef = new Firebase(Constants.FIREBASE_URL_USER_FRIENDS).child(userId);
        currentUserFriendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getKey());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    userList.add(user);
                    System.out.println("size: " + userList.size());
                }
                mFriendAdapter.setUserList(userList);
                mFriendAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG,
                        "The read failed: " +
                                firebaseError.getMessage());
            }
        });

        System.out.println("+++++++++++" + userList.size());
        mFriendAdapter = new FriendAdapter(this, R.layout.single_user_item, userList);

        /* Set adapter for the listView */
        mListView = (ListView) findViewById(R.id.list_view_friends_share);
        mListView.setAdapter(mFriendAdapter);
    }

    /**
     * Launch AddFriendActivity to find and add user to current user's friends list
     * when the button AddFriend is pressed
     */
    public void onAddFriendPressed(View view) {
        Intent intent = new Intent(ShareEventActivity.this, AddFriendActivity.class);
        startActivity(intent);
    }


    public void onConfirmButtonClicked(View view) {
        Intent intent = new Intent(ShareEventActivity.this, CreateNewEventActivity.class);
        List<User> checkedList = mFriendAdapter.getCheckedUserList();
        Log.v(LOG_TAG, new Gson().toJson(checkedList));
//        intent.putExtra("friendList", sharedEmailSet.toArray(new String[0]));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
