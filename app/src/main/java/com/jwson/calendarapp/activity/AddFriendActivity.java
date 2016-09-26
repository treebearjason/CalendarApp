package com.jwson.calendarapp.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.AutocompleteFriendAdapter;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;

public class AddFriendActivity extends AppCompatActivity {
    private EditText mEditTextAddFriendEmail;
    private AutocompleteFriendAdapter mFriendsAutocompleteAdapter;
    private String mInput;
    private ListView mListViewAutocomplete;
    private Firebase mUsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        /**
         * Create Firebase references
         */
        mUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFriendsAutocompleteAdapter != null) {
            mFriendsAutocompleteAdapter.cleanup();
        }
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        mListViewAutocomplete = (ListView) findViewById(R.id.list_view_friends_autocomplete);

        mFriendsAutocompleteAdapter = new AutocompleteFriendAdapter(AddFriendActivity.this, User.class,
                R.layout.single_autocomplete_item, mUsersRef, FirebaseAuth.getInstance().getCurrentUser().getUid());
        mListViewAutocomplete.setAdapter(mFriendsAutocompleteAdapter);

    }}
