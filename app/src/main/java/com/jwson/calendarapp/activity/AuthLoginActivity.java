package com.jwson.calendarapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.ParameterBuilder;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.PasswordlessLock;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.request.ParameterizableRequest;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class AuthLoginActivity extends AppCompatActivity{
    private PasswordlessLock passwordlessLock;
    private FirebaseAuth mAuth;

    private View rootLayout;
    private RadioGroup groupPasswordlessChannel;
    private CheckBox checkboxConnectionsPasswordless;


    private static final String TAG = "AuthLoginActivity";
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            startActivity(new Intent(AuthLoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_auth_login);
        rootLayout = findViewById(R.id.scrollView);

        Button btnShowLockPasswordless = (Button) findViewById(R.id.btn_show_lock_passwordless);
        btnShowLockPasswordless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordlessLock();
            }
        });
    }


    private void showPasswordlessLock() {
        final PasswordlessLock.Builder builder = PasswordlessLock.newBuilder(getAccount(), callback);
        builder.closable(false);
        builder.withAuthButtonSize(AuthButtonSize.SMALL);
        builder.useCode();
        builder.allowedConnections(generateConnections());

        passwordlessLock = builder.build(this);

        startActivity(passwordlessLock.newIntent(this));
    }

    private Auth0 getAccount() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        return auth0;
    }

    private List<String> generateConnections() {
        List<String> connections = new ArrayList<>();
        connections.add("sms");

        if (connections.isEmpty()) {
            connections.add("no-connection");
        }
        return connections;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (passwordlessLock != null) {
            passwordlessLock.onDestroy(this);
        }
    }

    /**
     * Shows a Snackbar on the bottom of the layout
     *
     * @param message the text to show.
     */
    @SuppressWarnings("ConstantConditions")
    private void showResult(String message) {
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private LockCallback callback = new AuthenticationCallback() {
        String idToken = "";
        String apiType = "firebase";
        String mCustomToken = "";
        String mobile = "";
        @Override
        public void onAuthentication(Credentials credentials) {
            showResult("OK > " + credentials.getIdToken());
            idToken = credentials.getIdToken();
            Log.d(TAG, "ID Token: "+ idToken);
            /**
             * Get Auth0 User Profile info
             */
            AuthenticationAPIClient client = passwordlessLock.getOptions().getAuthenticationAPIClient();
            client.tokenInfo(idToken).start(new BaseCallback<UserProfile, AuthenticationException>() {
                @Override
                public void onSuccess(UserProfile payload) {
                    mobile = payload.getName();
                    Log.i("user profile", new Gson().toJson(payload));
                }

                @Override
                public void onFailure(AuthenticationException error) {
                    error.printStackTrace();
                }
            });


            /**
             * Build Firebase custom token authentication
             */
            Map<String, Object> parameters = ParameterBuilder.newBuilder()
                    .set("id_token", idToken)
                    .set("api_type", apiType)
                    .asDictionary();
            client.delegation().addParameters(parameters).start(new BaseCallback<Map<String, Object>, AuthenticationException>() {
                @Override
                public void onSuccess(Map<String, Object> payload) {
                    mCustomToken = (String)payload.get("id_token");
                    Log.d(TAG, mCustomToken);
                    mAuth.signInWithCustomToken(mCustomToken).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithCustomToken:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithCustomToken", task.getException());
                                Toast.makeText(AuthLoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                /**
                                 * Save user to database
                                 */
                                User user = new User(mobile, new Date().getTime(), true);
                                user.setuId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                // Create a new document and add data
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users");

                                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                Log.d(TAG, "Registration Completed!");
                                Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(AuthenticationException error) {
                    Log.d(TAG,"Cannot login firebase!");
                    error.printStackTrace();
                }
            });

        }

        @Override
        public void onCanceled() {
            showResult("User pressed back.");
        }

        @Override
        public void onError(LockException error) {
            showResult(error.getMessage());
        }
    };

}



