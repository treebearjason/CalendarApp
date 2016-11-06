package com.jwson.calendarapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.GraphRequest;
//import com.facebook.GraphRequestAsyncTask;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.jwson.calendarapp.R;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "Login Auth";    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    private FirebaseAuth mAuth;

//    /* *************************************
//     *              FACEBOOK               *
//     ***************************************/
//    /* The login button for Facebook */
//    private LoginButton mFacebookLoginButton;
//    /* The callback manager for Facebook */
//    private CallbackManager mFacebookCallbackManager;
//    /* Used to track user logging in/out off Facebook */
//    private AccessTokenTracker mFacebookAccessTokenTracker;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mAuth.getCurrentUser().getUid());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        initializeScreen();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Signing In");
        mAuthProgressDialog.setMessage("Loading...");
        mAuthProgressDialog.setCancelable(false);

        // Click listeners
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.guest_sign_in_button).setOnClickListener(this);

//        mFacebookCallbackManager = CallbackManager.Factory.create();
//        mFacebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
//        mFacebookLoginButton.setReadPermissions("email", "public_profile", "user_friends");
//
//        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
//                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
//                        loginResult.getAccessToken(),
//                        "/me/friends",
//                        null,
//                        HttpMethod.GET,
//                        new GraphRequest.Callback() {
//                            public void onCompleted(GraphResponse response) {
//
//                                try {
//                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
//                                    Log.v("friend list", rawName.toString());
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).executeAsync();
//
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            signInWithEmail();
        } else if (i == R.id.register_button) {
            onSignUpPressed();
        } else if (i == R.id.guest_sign_in_button) {
            signInAnonymously();
        }
    }

    private void signInAnonymously() {
        showProgressDialog();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            showErrorToast("Authentication Failed");
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signInWithEmail() {
        if (!validateLinkForm()) {
            return;
        }
        // Get email and password from form
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            showErrorToast("Authentication Failed");
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public void onSignUpPressed() {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }


    private boolean validateLinkForm() {
        boolean valid = true;

        String email = mEditTextEmailInput.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEditTextEmailInput.setError("Required.");
            valid = false;
        } else {
            mEditTextEmailInput.setError(null);
        }

        String password = mEditTextPasswordInput.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEditTextPasswordInput.setError("Required.");
            valid = false;
        } else {
            mEditTextPasswordInput.setError(null);
        }

        return valid;
    }

//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication with Facebook failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                });
//    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Login Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

