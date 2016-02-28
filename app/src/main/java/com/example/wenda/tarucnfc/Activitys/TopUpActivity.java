package com.example.wenda.tarucnfc.Activitys;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.R;

public class TopUpActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback, View.OnClickListener {

    private TextView mTextViewAccountID;
    private TextView mTextViewFullName;
    private EditText mEditTextOtherAmount;
    private Spinner mSpinnerTopUpAmount;
    private Button mButtonConfirm;
    private LinearLayout mLinearLayoutOtherAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_top_up);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set finviewbyid
        mTextViewAccountID = (TextView) findViewById(R.id.text_view_accountID);
        mTextViewFullName = (TextView) findViewById(R.id.text_view_name);
        mEditTextOtherAmount = (EditText) findViewById(R.id.edit_text_other_amount);
        mSpinnerTopUpAmount = (Spinner) findViewById(R.id.spinner_topup_amount);
        mButtonConfirm = (Button) findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
        mLinearLayoutOtherAmount = (LinearLayout) findViewById(R.id.other_amount);
        mLinearLayoutOtherAmount.setVisibility(View.GONE);
        mTextViewAccountID.setText(getLoginDetail(this).getAccountID());
        mTextViewFullName.setText(getLoginDetail(this).getName());
        Log.d("track", " " + getLoginDetail(this).getName());

        mSpinnerTopUpAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (mSpinnerTopUpAmount.getSelectedItemPosition()) {
                    case 6:
                        mLinearLayoutOtherAmount.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        String accountIDAmount = null;
        switch (mSpinnerTopUpAmount.getSelectedItemPosition()){
            case 1:
                accountIDAmount = mTextViewAccountID.getText().toString() + mSpinnerTopUpAmount.getSelectedItem().toString();
                break;

            case 2:
                accountIDAmount = mTextViewAccountID.getText().toString() + mSpinnerTopUpAmount.getSelectedItem().toString();
                break;

            case 3:
                accountIDAmount = mTextViewAccountID.getText().toString() + mSpinnerTopUpAmount.getSelectedItem().toString();
                break;

            case 4:
                accountIDAmount = mTextViewAccountID.getText().toString() + mSpinnerTopUpAmount.getSelectedItem().toString();
                break;

            case 5:
                accountIDAmount = mTextViewAccountID.getText().toString() + mSpinnerTopUpAmount.getSelectedItem().toString();
                break;

            case 6:
                accountIDAmount = mTextViewAccountID.getText().toString() + mEditTextOtherAmount.getText().toString();
                break;
            default:
                break;
        }
        String message = accountIDAmount;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_confirm:
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.title2) .content(R.string.content)
                        .positiveText(R.string.agree).negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() { @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            shortToast(TopUpActivity.this, "Top Up Successful.");
                            finish();
                        }})
                            .show();

                break;

            default:
                break;
        }
    }
}