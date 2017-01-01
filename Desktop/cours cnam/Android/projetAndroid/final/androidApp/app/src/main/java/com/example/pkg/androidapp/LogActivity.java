package com.example.pkg.androidapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity  {

   private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world" };
    //Keep track of the login task to ensure we can cancel it if requested.
    //private LogActivity.UserLoginTask mAuthTask = null;
  // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private TextView Statut;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    DevicePolicyManager mDPM;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        if(ContextCompat.checkSelfPermission( getBaseContext(), "android.permission.READ_SMS")== PackageManager.PERMISSION_GRANTED);
        else

            ActivityCompat.requestPermissions(LogActivity.this,new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);


        mEmailView = (AutoCompleteTextView)findViewById(R.id.logEmailEdit);
        mPasswordView = (EditText)findViewById(R.id.logPassEdit);
        Statut = (TextView) findViewById(R.id.LogRegStatut);
        Button logLoginBtn = (Button) findViewById(R.id.logLoginBtn);
        logLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
                // retrieve data and check if pass and login are valid
            }});

        Button logRegisterBtn = (Button)findViewById(R.id.logRegisterBtn);
        logRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                //pass email to register activity
        }});

        if (isRegistred())
        { logRegisterBtn.setClickable(false);
            Statut.setText("You are registred   ");
        }
        else
         {logLoginBtn.setClickable(false);
             Statut.setText("You have to Register before activation  ");
          }

    //kahina
       // mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

        Button btn = (Button)findViewById(R.id.getLocation);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),  locationActivity.class);
                startActivity(intent);

                //mDPM.lockNow();
            }
        });
        //kahina
    }


  private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
   // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if ( !CheckIdentifiant(email, password)) cancel= true;
       if (cancel) {
            // form field with an error.
          // focusView.requestFocus();
            Toast.makeText(getApplicationContext(), "in  cancel = ok   ", Toast.LENGTH_LONG).show();
        } else {
            //showProgress(true);
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);



        }
    }

    private boolean isRegistred() {

        SharedPreferences prefs = getSharedPreferences(ConfigData.MY_PREFS_NAME, MODE_PRIVATE);
        boolean registred = prefs.getBoolean(ConfigData.REGISTRED, false);

        return registred;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private boolean CheckIdentifiant(String mail, String password)
    {
         boolean check=false;

        SharedPreferences prefs = getSharedPreferences(ConfigData.MY_PREFS_NAME, MODE_PRIVATE);
        String SavedMail = prefs.getString(ConfigData.EMAIL, null);
        String SavedPass = prefs.getString(ConfigData.PASSWORD, null);


        if (SavedMail != null && SavedPass!=null) {
            if (SavedMail.equals(mail) && SavedPass.equals(password) )
                check=true;
            Toast.makeText(getApplicationContext(), "check =true ", Toast.LENGTH_LONG).show();
 }

        Toast.makeText(getApplicationContext(), "check = "+check, Toast.LENGTH_LONG).show();


        return check;}


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }





}
