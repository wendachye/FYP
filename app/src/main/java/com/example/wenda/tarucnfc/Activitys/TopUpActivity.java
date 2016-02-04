package com.example.wenda.tarucnfc.Activitys;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.R;

public class TopUpActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback{

    private TextView mTextViewAccountID;
    private TextView mTextViewFullName;
    private EditText mEditTextOtherAmount;
    private Spinner mSpinnerTopUpAmount;
    private Button mButtonConfirm;

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

        // set finviewbyid
        mTextViewAccountID = (TextView) findViewById(R.id.text_view_accountID);
        mTextViewFullName = (TextView) findViewById(R.id.text_view_name);
        mEditTextOtherAmount = (EditText) findViewById(R.id.edit_text_other_amount);
        mSpinnerTopUpAmount = (Spinner) findViewById(R.id.spinner_topup_amount);
        mButtonConfirm = (Button) findViewById(R.id.button_confirm);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            //mEditText.setText("Sorry this device does not have NFC.");
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Sorry, this device does not have NFC.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String message = mTextViewAccountID.getText().toString();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}