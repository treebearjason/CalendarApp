package com.jwson.calendarapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAccountActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        Firebase.setAndroidContext(this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // My top posts by number of stars
        String myUserId = userId;
        Query userQuery = mDatabase.child("users").child(myUserId);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                TextView textView = (TextView) findViewById(R.id.my_account_details);
                StringBuilder sb = new StringBuilder();
                Date date = new Date(user.getCreateDate());
                SimpleDateFormat sf= new SimpleDateFormat("yyyy/MM/dd HH:mm");
                sb.append("Create Date: ").append(sf.format(date)).append("\n");
                sb.append("Mobile: ").append(user.getMobile()).append("\n");
//                sb.append("Firebase Token: ").append(user.getmToken()).append("\n");
                sb.append("Name: ").append(user.getName()).append("\n");
                sb.append("User Id: ").append(user.getuId()).append("\n");
                textView.setText(sb.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
