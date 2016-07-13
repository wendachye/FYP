package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Databases.Contracts.LoginContract;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.Login;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.Mail;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterNewAccount extends BaseActivity {

    private EditText mEditTextName, mEditTextNRICNo, mEditTextContact, mEditTextEmail;
    private String mName, mNRICNo, mContact, mEmail, PINString;
    private Account account = new Account();
    private Login login = new Login();
    private OfflineLogin offlineLogin = new OfflineLogin();
    private static final String SEND_EMAIL_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_account);

        //set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_register_new);

        //set home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextName = (EditText) findViewById(R.id.name);
        mEditTextNRICNo = (EditText) findViewById(R.id.nricNo);
        mEditTextContact = (EditText) findViewById(R.id.contactNo);
        mEditTextEmail = (EditText) findViewById(R.id.email);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_button, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        else if (id == R.id.saveButton) {
            // verify data
            verifyInput();
            if (isNetworkAvailable(this)) {
                generatePIN();
                UIUtils.getProgressDialog(RegisterNewAccount.this, "ON");
                sendVerifyEmail();
            } else {
                shortToast(this, "Network not available.");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;

        //Store integer in a string
        PINString = String.valueOf(randomPIN);

    }

    public void verifyInput() {
        mName = mEditTextName.getText().toString();
        mNRICNo = mEditTextNRICNo.getText().toString();
        mContact = mEditTextContact.getText().toString();
        mEmail = mEditTextEmail.getText().toString();

        try {
            account.verifyNRICNo(mNRICNo);
            account.verifyName(mName);
            account.verifyEmail(mEmail);
            account.verifyContactNo(mContact);
        } catch (InvalidInputException e) {
            shortToast(this, e.getInfo());
        }

    }

    public void sendVerifyEmail () {
        final Mail mail = new Mail("chyewd-wa12@student.tarc.edu.my", emailPassword);
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                String[] toArr = {mEmail};
                Log.d("track", "email " + mEmail);
                mail.setTo(toArr);
                mail.setFrom("chyewd-wa12@student.tarc.edu.my");
                mail.setSubject("Verify your account.");
                mail.setBody("Activation code: " + PINString + ".");

                try {
                    if(mail.send()) {
                        RegisterNewAccount.this.runOnUiThread(new Runnable() {
                            public void run() {
                                longToast("Verify email has been send to your email.");
                            }
                        });
                        UIUtils.getProgressDialog(RegisterNewAccount.this, "OFF");
                        finish();
                        Intent intent = new Intent(getApplicationContext(), VerifyAccount.class);
                        intent.putExtra("Code", PINString);
                        intent.putExtra("Name", mName);
                        intent.putExtra("NRIC", mNRICNo);
                        intent.putExtra("Contact", mContact);
                        intent.putExtra("Email", mEmail);
                        startActivity(intent);
                    } else {
                        RegisterNewAccount.this.runOnUiThread(new Runnable() {
                            public void run() {
                                longToast("Vefiry email was not sent to your email.");
                            }
                        });
                    }
                } catch(Exception e) {
                    //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                    Log.e("MailApp", "Could not send email", e);
                }
                return null;
            }
        }.execute();
    }
}
