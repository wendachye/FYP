package com.example.wenda.tarucnfc.Activitys;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.R;

import java.nio.charset.Charset;
import java.util.Locale;

public class TopUpActivity extends BaseActivity {

    NfcAdapter mNfcAdapter;
    private TextView messageText;
    private String payload = "";
    private EditText inputEditText;
    byte statusByte;
    private String inputText;
    private NdefMessage message;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;
    private String[][] mTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.top_up_title);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageText = (TextView) findViewById(R.id.messageText);

        // NFC adapter setup
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // Intent filters for exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        if (!mNfcAdapter.isEnabled()) {
            //nfc not support your device.
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            return;
        } else {

        }

    }

    public void onClickHandler(View view) {
        if (view.getId() == R.id.shareButton) {
            inputEditText = (EditText) findViewById(R.id.inputEditText);
            inputText = inputEditText.getText().toString();
            message = create_RTD_TEXT_NdefMessage(inputText);
            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
            Toast.makeText(this,"Touch another mobile to share message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
        else {
            mNfcAdapter.setNdefPushMessage(create_RTD_TEXT_NdefMessage(""), this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    void processIntent(Intent intent) {
        NdefMessage[] messages = getNdefMessages(getIntent());
        for (int i=0; i<messages.length; i++) {
            for (int j=0; j<messages[0].getRecords().length; j++) {
                NdefRecord record = messages[i].getRecords()[j];
                statusByte = record.getPayload()[0];
                int languageCodeLength = statusByte & 0x3F;
                int isUTF8 = statusByte - languageCodeLength;
                if (isUTF8 == 0x00) {
                    payload = new String(record.getPayload(), 1+languageCodeLength, record.getPayload().length-1-languageCodeLength, Charset.forName("UTF-8"));
                } else if (isUTF8 == -0x80) {
                    payload = new String(record.getPayload(), 1+languageCodeLength, record.getPayload().length-1-languageCodeLength, Charset.forName("UTF-16"));
                }
                messageText.setText("Text received: " + payload);
            }
        }
    }

    NdefMessage create_RTD_TEXT_NdefMessage(String inputText) {
        Locale locale = new Locale("es", "US");
        byte[] langByte = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        boolean encodeInUtf8 = false;
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        byte status = (byte) (utfBit + langByte.length);
        byte[] textBytes = inputText.getBytes(utfEncoding);
        byte[] data = new byte[1 + langByte.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langByte, 0, data, 1, langByte.length);
        System.arraycopy(textBytes, 0, data, 1 + langByte.length, textBytes.length);
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
        NdefMessage message = new NdefMessage(new NdefRecord[] { textRecord});
        return message;
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] msgs = null;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
             if (rawMsgs != null) {
                 msgs = new NdefMessage[rawMsgs.length];
                 for (int i=0; i<rawMsgs.length; i++) {
                     msgs[i] = (NdefMessage) rawMsgs[i];
                 }
             } else  {
                 byte[] empty = new byte[] {};
                 NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                 NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                 msgs = new NdefMessage[] {msg};
             }
        } else  {
            Log.d("Peer to peer 3", "Unknown intent.");
            finish();
        }
        return msgs;
    }

}
