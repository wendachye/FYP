package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditAuthorizationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private LinearLayout mLinearLayoutEnduser;
    private LinearLayout mLinearLayoutBackend;
    private TextView mTextViewAccountID;
    private TextView mTextViewAccountType;
    private TextView mTextViewStatus;
    private TextView mTextViewName;
    private Button mButtonDelete;
    private Spinner mSpinnerEnduser;
    private Spinner mSpinnerBackend;

    private String mAccountID;
    private Account account = new Account();
    private final static String GET_JSON_URL = "http://fypproject.host56.com/Account/edit_account_authorization_view.php";
    private final static String UPDATE_AUTHORIZATION_URL = "http://fypproject.host56.com/Account/update_account1.php";
    private final static String DELETE_AUTHORIZATION_URL = "http://fypproject.host56.com/Account/delete_account.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_authorization);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_edit_bus_route);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setfindviewbyid
        setFindviewbyid();

        mAccountID = getIntent().getStringExtra("AccountID");

        new GetJson(mAccountID).execute();
    }

    @Override
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
        } else if (id == R.id.saveButton) {
            updateAuthorization();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFindviewbyid() {
        mTextViewAccountID = (TextView) findViewById(R.id.text_view_accountID);
        mTextViewAccountType = (TextView) findViewById(R.id.text_view_accountType);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);
        mTextViewName = (TextView) findViewById(R.id.text_view_name);
        mSpinnerEnduser = (Spinner) findViewById(R.id.spinner_enduser_authorization);
        mSpinnerEnduser.setOnItemSelectedListener(this);
        mSpinnerBackend = (Spinner) findViewById(R.id.spinner_backend_authorization);
        mButtonDelete = (Button) findViewById(R.id.button_delete);
        mButtonDelete.setOnClickListener(this);
        mLinearLayoutEnduser = (LinearLayout) findViewById(R.id.enduser);
        mLinearLayoutBackend = (LinearLayout) findViewById(R.id.backend);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_delete:
                new DeleteAuthorization(mAccountID).execute();
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", accountID);
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setAccountID(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
            account.setName(jsonObject.getString(AccountContract.AccountRecord.KEY_NAME));
            account.setAccountType(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_TYPE));
            account.setAuthorization(jsonObject.getString(AccountContract.AccountRecord.KEY_AUTHORIZATION));
            account.setStatus(jsonObject.getString(AccountContract.AccountRecord.KEY_STATUS));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {
        mTextViewAccountID.setText(account.getAccountID());
        mTextViewName.setText(account.getName());
        mTextViewAccountType.setText(account.getAccountType());
        mTextViewStatus.setText(account.getStatus());
        if (account.getAccountType().equals("End User")) {
            mLinearLayoutBackend.setVisibility(View.GONE);
            mLinearLayoutEnduser.setVisibility(View.VISIBLE);
            switch (account.getAuthorization()){
                case "Student":
                    mSpinnerEnduser.setSelection(1);
                    break;

                case "Lecturer / Tutor":
                    mSpinnerEnduser.setSelection(2);
                    break;
            }
        } else {
            mLinearLayoutEnduser.setVisibility(View.GONE);
            mLinearLayoutBackend.setVisibility(View.VISIBLE);
            switch (account.getAuthorization()){
                case  "Admin":
                    mSpinnerBackend.setSelection(1);
                    break;

                case "Student Affair Department":
                    mSpinnerBackend.setSelection(2);
                    break;

                case "Examination Department":
                    mSpinnerBackend.setSelection(3);
                    break;

                case "Management Staff":
                    mSpinnerBackend.setSelection(4);
                    break;

                case "Food Stall Owner":
                    mSpinnerBackend.setSelection(5);
                    break;
            }
        }

    }

    //update account authorization
    private void updateAuthorization() {
        // set all the related values into account domain
        account.setAccountID(mTextViewAccountID.getText().toString());
        account.setName(mTextViewName.getText().toString());
        account.setAccountType(mTextViewAccountType.getText().toString());
        if (mTextViewAccountType.getText().toString().equals("Back End")) {
            account.setAuthorization(mSpinnerBackend.getSelectedItem().toString());
        } else {
            account.setAuthorization(mSpinnerEnduser.getSelectedItem().toString());
        }
        account.setStatus(mTextViewStatus.getText().toString());

        // check network
        if(isNetworkAvailable(this) == true) {
            new UpdateAuthorization(account).execute();
        } else {
            shortToast(this, "Network not available");
        }
    }

    public class UpdateAuthorization extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        Account account;

        public UpdateAuthorization(Account account) {
            this.account = account;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "OFF");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            shortToast(EditAuthorizationActivity.this, "Account Updated.");
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", this.account.getAccountID());
            data.put("name", this.account.getName());
            data.put("accountType", this.account.getAccountType());
            data.put("authorization", this.account.getAuthorization());
            data.put("status", this.account.getStatus());

            return requestHandler.sendPostRequest(UPDATE_AUTHORIZATION_URL, data);
        }
    }

    public class DeleteAuthorization extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String accountID;

        public DeleteAuthorization(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditAuthorizationActivity.this, "OFF");
            shortToast(EditAuthorizationActivity.this, "Delete Account Successful.");
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", accountID);

            return requestHandler.sendPostRequest(DELETE_AUTHORIZATION_URL, data);
        }
    }

}
