package com.example.wenda.tarucnfc.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePinCodeActivity extends BaseActivity {

    private static final String UPDATE_PINCODE_URL = "http://tarucandroid.comxa.com/Login/verify_password.php";
    private OfflineLogin offlineLogin = new OfflineLogin();
    private Account account;

    EditText mEditTextCurrent;
    EditText mEditTextNew;
    EditText mEditTextNewAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin_code);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.change_pincode_title);

        // set home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set findviewbyid
        setFindviewbyid();
    }

    public void setFindviewbyid () {
        mEditTextCurrent = (EditText) findViewById(R.id.current_pincode);
        mEditTextNew = (EditText) findViewById(R.id.new_pincode);
        mEditTextNewAgain = (EditText) findViewById(R.id.new_pincode_again);
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void verifyCurrentPincode(View view) {
        try {
            account.verifyPincode(mEditTextCurrent.getText().toString());
            new VerifyCurrentPincode(offlineLogin.getAccountID(), mEditTextCurrent.getText().toString()).execute();
        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class VerifyCurrentPincode extends AsyncTask<String, Void, String> {
        String accountID, currentPincode;
        RequestHandler rh = new RequestHandler();

        public VerifyCurrentPincode(String accountID, String currentPincode) {
            this.accountID = accountID;
            this.currentPincode = currentPincode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(ChangePinCodeActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(ChangePinCodeActivity.this, "OFF");
            //shortToast(EditPasswordActivity.this, json);
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                case RESPONSE_SUCCESS:
                    // login success, save login state and direct to main screen
                    //Intent intent = new Intent(getApplicationContext(), EditPasswordConfirmationActivity.class);
                    //intent.putExtra(KEY_ACCOUNT, account);
                    //startActivity(intent);
                    //finish();
                    break;
                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(ChangePinCodeActivity.this,"pincode incorrect");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_ACCOUNT_ID, accountID);
            data.put(KEY_PINCODE, currentPincode);

            return rh.sendPostRequest(UPDATE_PINCODE_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
