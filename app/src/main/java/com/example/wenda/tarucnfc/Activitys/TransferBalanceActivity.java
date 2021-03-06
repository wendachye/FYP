package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
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

import java.util.Calendar;
import java.util.HashMap;

public class TransferBalanceActivity extends BaseActivity {

    private EditText mEditTextAccountID, mEditTextAmount, mEditTextRemark;
    private String mTransferAccountID;
    private String mRecipientAccountID;
    private String mAmount;
    private String mRemark;

    private OfflineLogin offlineLogin = new OfflineLogin();
    private Account account = new Account();
    private String KEY_TRANSFER_ACCOUNT = "senderID";
    private String KEY_RECIPIENT_ACCOUNT = "recipientID";
    private String KEY_AMOUNT = "amount";
    private String KEY_REMARK = "remark";
    private String KEY_DATETIME = "dateTime";
    private static final String TRANSFER_BALANCE_URL = "http://fypproject.host56.com/Wallet/transfer_balance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_balance);

        // set current date time
        calendar = Calendar.getInstance();

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_transfer_balance);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set findviewbyid
        setFindviewbyid();
    }

    public void setFindviewbyid () {
        mEditTextAccountID = (EditText) findViewById(R.id.edit_text_accountID);
        mEditTextAmount = (EditText) findViewById(R.id.edit_text_amount);
        mEditTextRemark = (EditText) findViewById(R.id.edit_text_remark);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            Intent intent = new Intent(TransferBalanceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ConfirmButton(View view) throws InvalidInputException {
        mTransferAccountID = getLoginDetail(this).getAccountID();
        mAmount = mEditTextAmount.getText().toString();
        mRemark = mEditTextRemark.getText().toString();

        if (mEditTextAccountID.getText().toString().equals(mTransferAccountID)) {
            mEditTextAccountID.setText("");
            shortToast(TransferBalanceActivity.this, "Invalid Recipient Account.");
        } else {
            mRecipientAccountID = mEditTextAccountID.getText().toString();
            verifyReceiveAccount();
        }
    }

    public void verifyReceiveAccount() {
        try {
            account.verifyRecipientAccountID(mRecipientAccountID);

            if (isNetworkAvailable(this)) {
                new UpdateAccountBalance(mTransferAccountID, mRecipientAccountID, mAmount, mRemark).execute();
            } else {
                shortToast(this, "Network not available.");
            }

        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    // this one is get json
    public class UpdateAccountBalance extends AsyncTask<String, Void, String> {
        String transferAccountID, recipientAccount, amount, remark;
        RequestHandler rh = new RequestHandler();

        public UpdateAccountBalance(String transferAccountID, String recipientAccount, String amount, String remark) {
            this.transferAccountID = transferAccountID;
            this.recipientAccount = recipientAccount;
            this.amount = amount;
            this.remark = remark;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(TransferBalanceActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(TransferBalanceActivity.this, "OFF");
            extractJsonData(json);

            switch (offlineLogin.getLoginResponse()){
                // 1 = success
                case 1:
                    shortToast(TransferBalanceActivity.this, "Transfer successfully.");
                    Intent intent = new Intent(TransferBalanceActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                // 2 = account balance insufficient
                case 2:
                    shortToast(TransferBalanceActivity.this, "Current Account Balance Insufficient.");
                    mEditTextAmount.setText("");
                    break;

                // 3 = invalid recipient account
                case 3:
                    shortToast(TransferBalanceActivity.this, "Invalid Recipient Account.");
                    mEditTextAccountID.setText("");
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put(KEY_TRANSFER_ACCOUNT, transferAccountID);
            data.put(KEY_RECIPIENT_ACCOUNT, recipientAccount);
            data.put("transactionType", "Transfer");
            data.put(KEY_AMOUNT, amount);
            data.put(KEY_DATETIME, dateTimeFormat.format(calendar.getTime()));
            data.put(KEY_REMARK, remark);

            return rh.sendPostRequest(TRANSFER_BALANCE_URL, data);
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
