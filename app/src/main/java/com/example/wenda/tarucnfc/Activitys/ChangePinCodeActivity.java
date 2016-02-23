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

    private static final String UPDATE_PINCODE_URL = "http://fypproject.host56.com/Account/update_pincode.php";
    private OfflineLogin offlineLogin = new OfflineLogin();
    private Account account = new Account();

    private EditText mEditTextCurrent;
    private EditText mEditTextNew;
    private EditText mEditTextNewAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin_code);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_change_pincode);

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
            checkConfirmPincode();
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkConfirmPincode() {
        if (mEditTextNewAgain.getText().toString().equals(mEditTextNewAgain.getText().toString())) {
            verifyCurrentPincode();
        } else {
            shortToast(ChangePinCodeActivity.this, "New PIN Code doesn't match.");
            mEditTextNew.setText("");
            mEditTextNewAgain.setText("");
        }
    }

    public void verifyCurrentPincode() {
        try {
            Log.d("track", "pincode " + mEditTextCurrent.getText().toString());
            account.verifyPincode(mEditTextCurrent.getText().toString());
            account.verifyPincode(mEditTextNew.getText().toString());
            account.verifyPincode(mEditTextNewAgain.getText().toString());
            new UpdateCurrentPincode(getLoginDetail(this).getAccountID(), mEditTextCurrent.getText().toString(), mEditTextNew.getText().toString()).execute();
        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class UpdateCurrentPincode extends AsyncTask<String, Void, String> {
        String accountID, currentPincode, newPincode;
        RequestHandler rh = new RequestHandler();

        public UpdateCurrentPincode(String accountID, String currentPincode, String newPincode) {
            this.accountID = accountID;
            this.currentPincode = currentPincode;
            this.newPincode = newPincode;
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
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                case RESPONSE_SUCCESS:
                    shortToast(ChangePinCodeActivity.this,"PIN Code was changed.");
                    MainActivity.main.finish();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;
                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect
                    shortToast(ChangePinCodeActivity.this,"Current PIN Code Incorrect.");
                    mEditTextCurrent.setText("");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_ACCOUNT_ID, accountID);
            data.put(KEY_CURRENT_PINCODE, currentPincode);
            data.put(KEY_NEW_PINCODE, newPincode);

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
