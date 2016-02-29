package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import java.util.HashMap;

public class DisplayNFCActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextViewAccountID;
    private TextView mTextViewAmount;
    private String messageRecevied;
    private Button mButtonConfirm;
    private String amount, mbackendID;
    private static final String TOP_UP_BALANCE_URL = "http://fypproject.host56.com/Wallet/top_up_balance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nfc);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_top_up);

        mTextViewAccountID = (TextView)findViewById(R.id.text_view_accountID);
        mTextViewAmount = (TextView)findViewById(R.id.text_view_topupAmount);
        mButtonConfirm = (Button)findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);

        mbackendID = getLoginDetail(this).getAccountID();
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
            mTextViewAmount.setText(messageRecevied.substring(10,15));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_confirm:
                amount = mTextViewAmount.getText().toString().substring(2,5);
                new TopUpBalance(mbackendID, mTextViewAccountID.getText().toString(), amount).execute();
                break;

            default:
                break;
        }
    }

    public class TopUpBalance extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String backendID;
        String accountID;
        String amount;


        public TopUpBalance(String backendID, String accountID, String amount) {
            this.backendID = backendID;
            this.accountID = accountID;
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(DisplayNFCActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(DisplayNFCActivity.this, "OFF");
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();
            data.put("backendID", backendID);
            data.put("accountID", accountID);
            data.put("transactionType", "Top Up");
            data.put("amount", amount);
            data.put("dateTime", dateTimeFormat.format(calendar.getTime()));

            return requestHandler.sendPostRequest(TOP_UP_BALANCE_URL, data);
        }
    }
}
