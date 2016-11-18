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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.ParameterBuilder;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.AuthButtonSize;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.InitialScreen;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.PasswordlessLock;
import com.auth0.android.lock.UsernameStyle;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
public class AuthLoginActivity extends AppCompatActivity {
    private Lock lock;
    private PasswordlessLock passwordlessLock;
    private FirebaseAuth mAuth;

    private View rootLayout;
    private RadioGroup groupSubmitMode;
    private CheckBox checkboxClosable;
    private RadioGroup groupPasswordlessChannel;
    private RadioGroup groupPasswordlessMode;
    private CheckBox checkboxConnectionsDB;
    private CheckBox checkboxConnectionsEnterprise;
    private CheckBox checkboxConnectionsSocial;
    private CheckBox checkboxConnectionsPasswordless;
    private RadioGroup groupDefaultDB;
    private RadioGroup groupSocialStyle;
    private RadioGroup groupUsernameStyle;
    private CheckBox checkboxLoginAfterSignUp;
    private CheckBox checkboxScreenLogIn;
    private CheckBox checkboxScreenSignUp;
    private CheckBox checkboxScreenReset;
    private RadioGroup groupInitialScreen;

    private static final String TAG = "AuthActivity";
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mAuth.getCurrentUser().getUid());
            startActivity(new Intent(AuthLoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_auth_login);

        rootLayout = findViewById(R.id.scrollView);

        //Basic
        groupSubmitMode = (RadioGroup) findViewById(R.id.group_submitmode);
        checkboxClosable = (CheckBox) findViewById(R.id.checkbox_closable);

        checkboxConnectionsDB = (CheckBox) findViewById(R.id.checkbox_connections_db);
        checkboxConnectionsEnterprise = (CheckBox) findViewById(R.id.checkbox_connections_enterprise);
        checkboxConnectionsSocial = (CheckBox) findViewById(R.id.checkbox_connections_social);
        checkboxConnectionsPasswordless = (CheckBox) findViewById(R.id.checkbox_connections_Passwordless);

        groupPasswordlessChannel = (RadioGroup) findViewById(R.id.group_passwordless_channel);
        groupPasswordlessMode = (RadioGroup) findViewById(R.id.group_passwordless_mode);

        //Advanced
        groupDefaultDB = (RadioGroup) findViewById(R.id.group_default_db);
        groupSocialStyle = (RadioGroup) findViewById(R.id.group_social_style);
        groupUsernameStyle = (RadioGroup) findViewById(R.id.group_username_style);
        checkboxLoginAfterSignUp = (CheckBox) findViewById(R.id.checkbox_login_after_signup);

        checkboxScreenLogIn = (CheckBox) findViewById(R.id.checkbox_enable_login);
        checkboxScreenSignUp = (CheckBox) findViewById(R.id.checkbox_enable_signup);
        checkboxScreenReset = (CheckBox) findViewById(R.id.checkbox_enable_reset);
        groupInitialScreen = (RadioGroup) findViewById(R.id.group_initial_screen);

        //Buttons
        final LinearLayout advancedContainer = (LinearLayout) findViewById(R.id.advanced_container);
        CheckBox checkboxShowAdvanced = (CheckBox) findViewById(R.id.checkbox_show_advanced);
        checkboxShowAdvanced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                advancedContainer.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

        Button btnShowLockClassic = (Button) findViewById(R.id.btn_show_lock_classic);
        btnShowLockClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClassicLock();
            }
        });

        Button btnShowLockPasswordless = (Button) findViewById(R.id.btn_show_lock_passwordless);
        btnShowLockPasswordless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasswordlessLock();
            }
        });
    }

    private void showClassicLock() {
        final Lock.Builder builder = Lock.newBuilder(getAccount(), callback);
        builder.closable(checkboxClosable.isChecked());
        builder.useLabeledSubmitButton(groupSubmitMode.getCheckedRadioButtonId() == R.id.radio_use_label);
        builder.loginAfterSignUp(checkboxLoginAfterSignUp.isChecked());

        if (groupSocialStyle.getCheckedRadioButtonId() == R.id.radio_social_style_big) {
            builder.withAuthButtonSize(AuthButtonSize.BIG);
        } else if (groupSocialStyle.getCheckedRadioButtonId() == R.id.radio_social_style_small) {
            builder.withAuthButtonSize(AuthButtonSize.SMALL);
        }

        if (groupUsernameStyle.getCheckedRadioButtonId() == R.id.radio_username_style_email) {
            builder.withUsernameStyle(UsernameStyle.EMAIL);
        } else if (groupUsernameStyle.getCheckedRadioButtonId() == R.id.radio_username_style_username) {
            builder.withUsernameStyle(UsernameStyle.USERNAME);
        }

        builder.allowLogIn(checkboxScreenLogIn.isChecked());
        builder.allowSignUp(checkboxScreenSignUp.isChecked());
        builder.allowForgotPassword(checkboxScreenReset.isChecked());

        if (groupInitialScreen.getCheckedRadioButtonId() == R.id.radio_initial_reset) {
            builder.initialScreen(InitialScreen.FORGOT_PASSWORD);
        } else if (groupInitialScreen.getCheckedRadioButtonId() == R.id.radio_initial_signup) {
            builder.initialScreen(InitialScreen.SIGN_UP);
        } else {
            builder.initialScreen(InitialScreen.LOG_IN);
        }

        builder.allowedConnections(generateConnections());
        if (checkboxConnectionsDB.isChecked()) {
            if (groupDefaultDB.getCheckedRadioButtonId() == R.id.radio_default_db_policy) {
                builder.setDefaultDatabaseConnection("with-strength");
            } else if (groupDefaultDB.getCheckedRadioButtonId() == R.id.radio_default_db_mfa) {
                builder.setDefaultDatabaseConnection("mfa-connection");
            } else {
                builder.setDefaultDatabaseConnection("Username-Password-Authentication");
            }
        }
        lock = builder.build(this);

        startActivity(lock.newIntent(this));
    }


    private void showPasswordlessLock() {
        final PasswordlessLock.Builder builder = PasswordlessLock.newBuilder(getAccount(), callback);
        builder.closable(checkboxClosable.isChecked());

        if (groupSocialStyle.getCheckedRadioButtonId() == R.id.radio_social_style_big) {
            builder.withAuthButtonSize(AuthButtonSize.BIG);
        } else if (groupSocialStyle.getCheckedRadioButtonId() == R.id.radio_social_style_small) {
            builder.withAuthButtonSize(AuthButtonSize.SMALL);
        }

        if (groupPasswordlessMode.getCheckedRadioButtonId() == R.id.radio_use_link) {
            builder.useLink();
        } else {
            builder.useCode();
        }

        builder.allowedConnections(generateConnections());

        passwordlessLock = builder.build(this);

        startActivity(passwordlessLock.newIntent(this));
    }

    private Auth0 getAccount() {
        return new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
    }

    private List<String> generateConnections() {
        List<String> connections = new ArrayList<>();
        if (checkboxConnectionsDB.isChecked()) {
            connections.add("Username-Password-Authentication");
            connections.add("mfa-connection");
            connections.add("with-strength");
        }
        if (checkboxConnectionsEnterprise.isChecked()) {
            connections.add("ad");
            connections.add("another");
        }
        if (checkboxConnectionsSocial.isChecked()) {
            connections.add("google-oauth2");
            connections.add("twitter");
            connections.add("facebook");
        }
        if (checkboxConnectionsPasswordless.isChecked()) {
            connections.add(groupPasswordlessChannel.getCheckedRadioButtonId() == R.id.radio_use_sms ? "sms" : "email");
        }
        if (connections.isEmpty()) {
            connections.add("no-connection");
        }
        return connections;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lock != null) {
            lock.onDestroy(this);
        }
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
        @Override
        public void onAuthentication(Credentials credentials) {
            showResult("OK > " + credentials.getIdToken());
            idToken = credentials.getIdToken();

            Map<String, Object> parameters = ParameterBuilder.newBuilder()
                    .set("id_token", idToken)
                    .set("api_type", apiType)
                    .asDictionary();
            AuthenticationAPIClient client = passwordlessLock.getOptions().getAuthenticationAPIClient();
            client.delegation().addParameters(parameters).start(new BaseCallback<Map<String, Object>, AuthenticationException>() {
                @Override
                public void onSuccess(Map<String, Object> payload) {
                    Log.d(TAG, new Gson().toJson(payload));
                    mCustomToken = (String)payload.get("id_token");

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
                                String mobile = "1342425" ;
                                User user = new User(mobile, new Date().getTime(), true);
                                user.setuId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                // Create a new document and add data
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users");

                                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                                Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(AuthenticationException error) {
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



