package com.jwson.calendarapp.adapter;

import com.jwson.calendarapp.domain.User;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jwson.calendarapp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.jwson.calendarapp.utils.Constants;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 9/26/2016.
 */
public class AutocompleteFriendAdapter extends FirebaseListAdapter<User> {
    private String mCurrentUserId;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public AutocompleteFriendAdapter(Activity activity, Class<User> modelClass, int modelLayout,
                                     Query ref, String currentUserId) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mCurrentUserId = currentUserId;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_autocomplete_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final User user) {
         /* Get friends email textview and set it's text to user.email() */
        TextView textViewFriendEmail = (TextView) view.findViewById(R.id.text_view_autocomplete_item);
        textViewFriendEmail.setText(user.getEmail());

        /**
         * Set the onClickListener to a single list item
         * If selected email is not friend already and if it is not the
         * current user's email, we add selected user to current user's friends
         */
        textViewFriendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * If selected user is not current user proceed
                 */
                if (isNotCurrentUser(user)) {

                    Firebase currentUserFriendsRef = new Firebase(Constants.FIREBASE_URL_USER_FRIENDS).child(mCurrentUserId);
                    final Firebase friendRef = currentUserFriendsRef.child(user.getuId());

                    /**
                     * Add listener for single value event to perform a one time operation
                     */
                    friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            /**
                             * Add selected user to current user's friends if not in friends yet
                             */
                            if (isNotAlreadyAdded(dataSnapshot, user)) {
                                friendRef.setValue(user);
                                mActivity.finish();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.e(mActivity.getClass().getSimpleName(),
                                    "The read failed: " +
                                            firebaseError.getMessage());
                        }
                    });

                }
            }
        });

    }

    private boolean isNotCurrentUser(User user) {
        if (user.getuId().equals(mCurrentUserId)) {
            /* Toast appropriate error message if the user is trying to add themselves  */
            Toast.makeText(mActivity,
                    "You can't add yourself as a friend.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isNotAlreadyAdded(DataSnapshot dataSnapshot, User user) {
        if (dataSnapshot.getValue(User.class) != null) {
            /* Toast appropriate error message if the user is already a friend of the user */
            String friendError = String.format("%s is already your friend!",
                    user.getName());

            Toast.makeText(mActivity,
                    friendError,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}