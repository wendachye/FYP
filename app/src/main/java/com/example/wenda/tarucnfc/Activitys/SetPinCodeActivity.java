package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SetPinCodeActivity extends BaseActivity {

    private EditText mEditTextNew;
    private EditText mEditTextNewAgain;

    private Account account = new Account();
    private static final String INSERT_PINCODE_URL = "http://fypproject.host56.com/Account/add_pincode.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin_code);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_set_pincode);

        // set home button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            verifyPincode();
        } else {
            shortToast(SetPinCodeActivity.this, "PIN Code doesn't match.");
            mEditTextNew.setText("");
            mEditTextNewAgain.setText("");
        }
    }

    public void verifyPincode() {
        try {
            account.verifyPincode(mEditTextNew.getText().toString());
            account.verifyPincode(mEditTextNewAgain.getText().toString());
            if (isNetworkAvailable(this)) {
                new InsertPincode(getLoginDetail(this).getAccountID(), mEditTextNew.getText().toString()).execute();
            } else {
                shortToast(this, "Network not available.");
            }
        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class InsertPincode extends AsyncTask<String, Void, String> {
        String accountID, newPincode;
        RequestHandler rh = new RequestHandler();

        public InsertPincode(String accountID, String newPincode) {
            this.accountID = accountID;
            this.newPincode = newPincode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(SetPinCodeActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(SetPinCodeActivity.this, "OFF");

            extractJsonData(json);

            switch (account.getResponse()){
                case RESPONSE_SUCCESS:
                    shortToast(SetPinCodeActivity.this, "PIN Code was changed.");
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    break;

                case RESPONSE_PASSWORD_INCORRECT:
                    // password incorrect

                    break;
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_ACCOUNT_ID, accountID);
            data.put(KEY_NEW_PINCODE, newPincode);

            return rh.sendPostRequest(INSERT_PINCODE_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
