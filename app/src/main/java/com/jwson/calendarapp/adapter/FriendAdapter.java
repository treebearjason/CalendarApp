package com.jwson.calendarapp.adapter;

/**
 * Created by user on 9/26/2016.
 */
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class FriendAdapter extends FirebaseListAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private String mListId;
    private Firebase mFirebaseRef;
    private HashMap<String, User> mFriendsAdded;
    private HashMap <Firebase, ValueEventListener> mLocationListenerMap;


    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public FriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                         Query ref, String listId) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListId = listId;
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mLocationListenerMap = new HashMap<Firebase, ValueEventListener>();
    }
    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_user_item.xml
     * populateView also handles data changes and updates the listView accordingly

     */
    @Override
    protected void populateView(View view, final User friend) {
        ((TextView) view.findViewById(R.id.user_email)).setText(friend.getEmail());
        final ImageButton buttonToggleShare = (ImageButton) view.findViewById(R.id.button_toggle_share);

    }

    /**
     * Public method that is used to pass SharedUsers when they are loaded in ValueEventListener
     */
    public void setSharedWithUsers(HashMap<String, User> sharedUsersList) {
        this.mFriendsAdded = sharedUsersList;
        this.notifyDataSetChanged();
    }


    @Override
    public void cleanup() {
        super.cleanup();
        /* Clean up the event listeners */
        for (HashMap.Entry<Firebase, ValueEventListener> listenerToClean : mLocationListenerMap.entrySet())
        {
            listenerToClean.getKey().removeEventListener(listenerToClean.getValue());
        }

    }
}
