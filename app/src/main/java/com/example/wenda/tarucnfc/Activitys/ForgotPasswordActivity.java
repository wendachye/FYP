package com.example.wenda.tarucnfc.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract.AccountRecord;
import com.example.wenda.tarucnfc.Databases.Contracts.LoginContract.LoginRecord;
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

public class ForgotPasswordActivity extends BaseActivity {

    private EditText mEditTextAccountID, mEditTextNRICNo;
    private String mEmailAddress;
    private String mPassword;
    private String mAccountID, mNRICNo;
    private Account account = new Account();
    private Login login = new Login();
    private OfflineLogin offlineLogin = new OfflineLogin();
    private static final String GET_EMAIL_URL = "http://fypproject.host56.com/Login/get_emailAddress.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_forgot_password);

        //set home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextAccountID = (EditText) findViewById(R.id.accountID);
        mEditTextNRICNo = (EditText) findViewById(R.id.nricNo);
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
                new get_email_address(mAccountID, mNRICNo).execute();
            } else {
                shortToast(this, "Network not available.");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void verifyInput() {
        mAccountID = mEditTextAccountID.getText().toString();
        mNRICNo = mEditTextNRICNo.getText().toString();

        try {
            account.verifyNRICNo(mNRICNo);
            account.verifyAccountID(mAccountID);
        } catch (InvalidInputException e) {
            shortToast(this, e.getInfo());
        }

    }

    // this one is get json
    public class get_email_address extends AsyncTask<String, Void, String> {
        String accountID, nricNo;
        RequestHandler rh = new RequestHandler();

        public get_email_address(String accountID, String nricNo) {
            this.accountID = accountID;
            this.nricNo = nricNo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(ForgotPasswordActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                // correct accountID & nricNo
                case 1:
                    mEmailAddress = account.getEmailAddress();
                    mPassword = login.getPassword();
                    sendForgotPasswordEmail();
                    break;

                // incorrect accountID & nricNo
                case 2:
                    shortToast(ForgotPasswordActivity.this,"Account ID or NRIC.No is Incorrect.");
                    UIUtils.getProgressDialog(ForgotPasswordActivity.this, "OFF");
                    mEditTextAccountID.setText("");
                    mEditTextNRICNo.setText("");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, accountID);
            data.put("NRICNo", nricNo);

            return rh.sendPostRequest(GET_EMAIL_URL, data);
        }
    }

    private void extractJsonData(String json) {
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setEmailAddress(jsonObject.getString(AccountRecord.KEY_EMAIL_ADDRESS));
            login.setPassword(jsonObject.getString(LoginRecord.KEY_PASSWORD));
            offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordEmail () {
        final Mail mail = new Mail("chyewd-wa12@student.tarc.edu.my", emailPassword);
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                String[] toArr = {mEmailAddress};
                Log.d("track", "email " + mEmailAddress);
                mail.setTo(toArr);
                mail.setFrom("chyewd-wa12@student.tarc.edu.my");
                mail.setSubject("Retrieve TARUC Android Password.");
                mail.setBody("This is your password: " + mPassword + ".");

                try {
                    if(mail.send()) {
                        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                longToast("Password was sent to your email successfully");
                            }
                        });
                        UIUtils.getProgressDialog(ForgotPasswordActivity.this, "OFF");
                        finish();
                    } else {
                        ForgotPasswordActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                longToast("Password was not sent to your email.");
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
