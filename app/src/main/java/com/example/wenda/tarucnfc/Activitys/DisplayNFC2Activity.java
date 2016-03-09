package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import java.util.Calendar;
import java.util.HashMap;

public class DisplayNFC2Activity extends BaseActivity {

    private String messageRecevied;
    private TextView mTextViewAccountID;
    private TextView mTextViewAmount;
    private String amount, mReceipientID;
    private static final String TRANSFER_BALANCE_URL = "http://fypproject.host56.com/Wallet/transfer_balance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nfc2);

        // set current date time
        calendar = Calendar.getInstance();

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_transaction1);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReceipientID = getLoginDetail(this).getAccountID();
        mTextViewAccountID = (TextView)findViewById(R.id.text_view_accountID);
        mTextViewAmount = (TextView)findViewById(R.id.text_view_amount);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            messageRecevied = new String(message.getRecords()[0].getPayload());
            mTextViewAccountID.setText(messageRecevied.substring(0,10));
            mTextViewAmount.setText(messageRecevied.substring(13, 15));

            new UpdateAccountBalance(mTextViewAccountID.getText().toString(), mReceipientID, mTextViewAmount.getText().toString(), "").execute();
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
            //UIUtils.getProgressDialog(DisplayNFC2Activity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //UIUtils.getProgressDialog(DisplayNFC2Activity.this, "OFF");
            //extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put("senderID", transferAccountID);
            data.put("recipientID", recipientAccount);
            data.put("transactionType", "Transfer");
            data.put("amount", amount);
            data.put("dateTime", dateTimeFormat.format(calendar.getTime()));
            data.put("remark", remark);

            return rh.sendPostRequest(TRANSFER_BALANCE_URL, data);
        }
    }
}
