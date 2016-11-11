package com.jwson.calendarapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auth0.api.ParameterBuilder;
//import com.auth0.api.authentication.AuthenticationAPIClient;
import com.auth0.api.callback.BaseCallback;
import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
//import com.auth0.lock.LockContext;
import com.auth0.lock.sms.LockSMSActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class AuthLoginActivity extends Activity {
    private static final String TAG = "AuthActicity";
    private LocalBroadcastManager broadcastManager;
    private FirebaseAuth mAuth;
    private BroadcastReceiver authenticationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UserProfile profile = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
            Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);
            String idToken   = token.getIdToken();
            Log.i(TAG, "User " + profile.getName() + " logged in");

            signInWithCustomToken(profile, idToken);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mAuth.getCurrentUser().getUid());
            startActivity(new Intent(AuthLoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_auth_login);
        //Customize your activity
        Button tryButton = (Button) findViewById(R.id.try_it_button);
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthLoginActivity.this,  LockSMSActivity.class));
            }
        });

        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(authenticationReceiver, new IntentFilter(Lock.AUTHENTICATION_ACTION));
    }

    /**
     * Sign in Firebase with token
     */
    private void signInWithCustomToken(UserProfile profile, String idToken){
//        Lock lock = LockContext.getLock(this);
//        AuthenticationAPIClient client = lock.getAuthenticationAPIClient();
//        String apiType = "firebase";
//        Map<String, Object> parameters = ParameterBuilder.newEmptyBuilder()
//                .set("id_token", idToken)
//                .set("api_type", apiType)
//                .asDictionary();
//        client
//                .delegation()
//                .addParameters(parameters).start(new BaseCallback<Map<String, Object>>() {
//            @Override
//            public void onSuccess(Map<String, Object> payload) {
//                //Your Firebase token will be in payload
//                Toast.makeText(AuthLoginActivity.this, "Authentication succesful.",
//                        Toast.LENGTH_SHORT).show();
//                Log.d(TAG, new Gson().toJson(payload));
//            }
//
//            @Override
//            public void onFailure(Throwable error) {
//                //Delegation call failed
//            }
//        });

//        mAuth.signInWithCustomToken(mCustomToken)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCustomToken:onComplete:" + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCustomToken", task.getException());
//                            Toast.makeText(AuthLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }else {
//                            /**
//                             * Save user to database
//                             */
//
//                            User user = new User(mobile, new Date().getTime(), true);
//                            user.setuId(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                            // Create a new document and add data
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference myRef = database.getReference("users");
//
//                            myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
//
//                            Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(authenticationReceiver);
    }
}



