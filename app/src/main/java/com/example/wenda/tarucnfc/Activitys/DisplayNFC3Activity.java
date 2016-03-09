package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;

public class DisplayNFC3Activity extends BaseActivity {

    private String messageRecevied;
    private TextView mTextViewAccountID;
    private TextView mTextViewAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nfc3);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_transaction1);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            mTextViewAmount.setText(messageRecevied.substring(10,15));
        }
    }
}
