package com.jwson.calendarapp.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.AutocompleteFriendAdapter;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
    private EditText mEditTextAddFriendEmail;
    private AutocompleteFriendAdapter mFriendsAutocompleteAdapter;
    private String mInput;
    private ListView mListViewAutocomplete;
    private ListView phoneListView;
    private Cursor cursor;
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
        /**
         * Firebase existing users
         */
        mListViewAutocomplete = (ListView) findViewById(R.id.list_view_friends_autocomplete);

        mFriendsAutocompleteAdapter = new AutocompleteFriendAdapter(AddFriendActivity.this, User.class,
                R.layout.single_autocomplete_item, mUsersRef, FirebaseAuth.getInstance().getCurrentUser().getUid());
        mListViewAutocomplete.setAdapter(mFriendsAutocompleteAdapter);

        /**
         * Phone Contact List
         */
        phoneListView = (ListView) findViewById(R.id.list_view_phone_contact);
        getAndroidContacts();

    }

    public void getAndroidContacts() {
        ArrayList<AndroidContacts> contactList = new ArrayList<>();

        Cursor cursorAndroidContacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursorAndroidContacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
        System.out.println("size of contact lists: " + cursorAndroidContacts.getCount());

        if (cursorAndroidContacts.getCount() > 0) {

            while (cursorAndroidContacts.moveToNext()) {

                AndroidContacts androidContact = new AndroidContacts();
                String contact_id = cursorAndroidContacts.getString(cursorAndroidContacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_name = cursorAndroidContacts.getString(cursorAndroidContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                androidContact.contactName = contact_name;

                int hasPhoneNumber = Integer.parseInt(cursorAndroidContacts.getString(cursorAndroidContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                            , null
                            , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"
                            , new String[]{contact_id}
                            , null);

                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        androidContact.phoneNum = phoneNumber;

                    }
                    phoneCursor.close();
                }
                if(!StringUtils.isEmpty(androidContact.contactName) && !StringUtils.isEmpty(androidContact.phoneNum))
                    contactList.add(androidContact);
            }
            System.out.println("array contact lists: " + contactList.size());
            AndroidContactAdapter adapter = new AndroidContactAdapter(this, contactList);
            phoneListView.setAdapter(adapter);
        }
    }

    private class AndroidContacts {
        public String contactName = "";
        public String phoneNum = "";
        public int android_contact_ID=0;
    }

    private class AndroidContactAdapter extends BaseAdapter {

        Context mContext;
        List<AndroidContacts> mContactList;

        public AndroidContactAdapter(Context mContext, List<AndroidContacts> mContact) {
            this.mContext = mContext;
            this.mContactList = mContact;
        }

        @Override
        public int getCount() {
            return mContactList.size();
        }

        @Override
        public Object getItem(int position) {
            return mContactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.item_phone_contact, null);
            TextView contactNameTextView = (TextView) view.findViewById(R.id.phone_userName);
            TextView phoneNumTextView = (TextView) view.findViewById(R.id.phone_number);
            String name = mContactList.get(position).contactName;
            String phone = mContactList.get(position).phoneNum;

            contactNameTextView.setText(mContactList.get(position).contactName);
            phoneNumTextView.setText(mContactList.get(position).phoneNum);

            view.setTag(mContactList.get(position).contactName);
            return view;
        }
    }
}
