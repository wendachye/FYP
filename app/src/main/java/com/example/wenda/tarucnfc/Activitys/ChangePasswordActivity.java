package com.example.wenda.tarucnfc.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Domains.Login;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePasswordActivity extends BaseActivity {

    private static final String UPDATE_PASSWORD_URL = "http://tarucandroid.comxa.com/Login/update_password.php";

    private Login login = new Login();
    private OfflineLogin offlineLogin = new OfflineLogin();

    EditText mEditTextCurrent;
    EditText mEditTextNew;
    EditText mEditTextNewAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.change_password_title);

        // set home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set findviewbyid
        setFindviewbyid();
    }

    public void setFindviewbyid() {
        mEditTextCurrent = (EditText) findViewById(R.id.current_password);
        mEditTextNew = (EditText) findViewById(R.id.new_password);
        mEditTextNewAgain = (EditText) findViewById(R.id.new_password_again);
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
            Log.d("track", "pass1 ");
            verifyCurrentPassword();
        }

        return super.onOptionsItemSelected(item);
    }

    public void verifyCurrentPassword() {
        try {
            Log.d("track", "pass2 ");
            login.verifyPassword(mEditTextCurrent.getText().toString());
            Log.d("track", "accountID " + getLoginDetail(this).getAccountID());
            new VerifyCurrentPassword(getLoginDetail(this).getAccountID(), mEditTextCurrent.getText().toString(), mEditTextNew.getText().toString()).execute();
        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class VerifyCurrentPassword extends AsyncTask<String, Void, String> {
        String accountID, currentPassword, newPassword;
        RequestHandler rh = new RequestHandler();

        public VerifyCurrentPassword(String accountID, String currentPassword, String newPassword) {
            Log.d("track", "pass4 ");
            this.accountID = accountID;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected void onPreExecute() {
            Log.d("track", "pass5 ");
            super.onPreExecute();
            UIUtils.getProgressDialog(ChangePasswordActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            Log.d("track", "pass6 ");
            super.onPostExecute(json);
            UIUtils.getProgressDialog(ChangePasswordActivity.this, "OFF");
            shortToast(ChangePasswordActivity.this, json);
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                case RESPONSE_SUCCESS:
                    // login success, save login state and direct to main screen
                    //Intent intent = new Intent(getApplicationContext(), EditPasswordConfirmationActivity.class);
                    //intent.putExtra(KEY_ACCOUNT, account);
                    //startActivity(intent);
                    //finish();
                    shortToast(ChangePasswordActivity.this,"password correct");
                    break;
                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(ChangePasswordActivity.this,"password incorrect");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_ACCOUNT_ID, accountID);
            data.put(KEY_CURRENT_PASSWORD, currentPassword);
            data.put(KEY_NEW_PASSWORD, newPassword);

            return rh.sendPostRequest(UPDATE_PASSWORD_URL, data);
        }
    }

    private void extractJsonData(String json) {

        Log.d("track", "pass7 ");

        try {
            Log.d("track", "pass8 ");
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));
            Log.d("track", "password " + jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
