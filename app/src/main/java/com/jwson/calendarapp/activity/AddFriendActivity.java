package com.jwson.calendarapp.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.adapter.AndroidContactAdapter;
import com.jwson.calendarapp.adapter.AutocompleteFriendAdapter;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;
import okhttp3.Call;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddFriendActivity extends AppCompatActivity {
    private AutocompleteFriendAdapter mFriendsAutocompleteAdapter;
    private ListView mListViewAutocomplete;
    private ListView phoneListView;
    private Firebase mUsersRef;
    private OkHttpClient mClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);

        initializeScreen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFriendsAutocompleteAdapter != null) {
            mFriendsAutocompleteAdapter.cleanup();
        }
    }

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
        final ArrayList<AndroidContacts> contactList = new ArrayList<>();

        Cursor cursorAndroidContacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursorAndroidContacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        } catch (Exception ex) {
            Log.e("Error on contact", ex.getMessage());
        }
        System.out.println("size of contact lists: " + cursorAndroidContacts.getCount());

        if(cursorAndroidContacts != null){
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

                AndroidContactAdapter adapter = new AndroidContactAdapter(this, contactList);
                phoneListView.setAdapter(adapter);
                phoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AndroidContacts contact = contactList.get(position);
                        final String name = contact.getContactName();
                        final String to = contact.getPhoneNum();

                        new AlertDialog.Builder(AddFriendActivity.this)
                                .setTitle("SMS Invitation")
                                .setMessage("Invite " + name + " to join Calends?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            post(Constants.NGROK_URL, to, "Hi " + name,  new  Callback(){

                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    e.printStackTrace();
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(),"SMS Sent!",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                });
            }
        }else{
            Log.e("AddFriendActivity","Contact List Permission denied!!");
        }

    }

    public class AndroidContacts {
        public String contactName = "";
        public String phoneNum = "";

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }
    }



    protected Call post(String url, String to, String body , Callback callback) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("To", to)
                .add("Body", body)
                .build();
        Request request = new Request.Builder()
                .url(url).post(formBody).build();
        Call response = mClient.newCall(request);
        response.enqueue(callback);
        return response;

    }

}
