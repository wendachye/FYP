package com.example.wenda.tarucnfc.Activitys;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;

public class DisplayNFCActivity extends BaseActivity {

    private TextView bTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_nfc);
        bTextView = (TextView)findViewById(R.id.text_view_TransacID);
    }


    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            bTextView.setText(new String(message.getRecords()[0].getPayload()));

        } else
            bTextView.setText("Waiting for NDEF Message");

    }
}
