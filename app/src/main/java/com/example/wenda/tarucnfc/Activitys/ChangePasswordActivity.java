package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
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

    private static final String UPDATE_PASSWORD_URL = "http://fypproject.host56.com/Account/update_password.php";

    private Login login = new Login();
    private OfflineLogin offlineLogin = new OfflineLogin();

    private EditText mEditTextCurrent;
    private EditText mEditTextNew;
    private EditText mEditTextNewAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_change_password);

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
            // verify new password
            checkConfirmPassword();
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkConfirmPassword() {
        if (mEditTextNewAgain.getText().toString().equals(mEditTextNew.getText().toString())) {
            // verify current password
            verifyCurrentPassword();
        } else {
            shortToast(ChangePasswordActivity.this, "New Password doesn't match.");
            mEditTextNew.setText("");
            mEditTextNewAgain.setText("");
        }
    }

    public void verifyCurrentPassword() {
        try {
            login.verifyPassword(mEditTextCurrent.getText().toString());
            login.verifyPassword(mEditTextNew.getText().toString());
            login.verifyPassword(mEditTextNewAgain.getText().toString());

            if (isNetworkAvailable(this)) {
                new UpdateCurrentPassword(getLoginDetail(this).getAccountID(), mEditTextCurrent.getText().toString(), mEditTextNew.getText().toString()).execute();
            } else {
                shortToast(this, "Network not available.");
            }

        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class UpdateCurrentPassword extends AsyncTask<String, Void, String> {
        String accountID, currentPassword, newPassword;
        RequestHandler rh = new RequestHandler();

        public UpdateCurrentPassword(String accountID, String currentPassword, String newPassword) {
            this.accountID = accountID;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(ChangePasswordActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(ChangePasswordActivity.this, "OFF");
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                case RESPONSE_SUCCESS:
                    shortToast(ChangePasswordActivity.this,"Password was changed.");
                    MainActivity.main.finish();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;
                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(ChangePasswordActivity.this,"Current Password Incorrect");
                    mEditTextCurrent.setText("");
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
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));
            Log.d("track", "password " + jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
