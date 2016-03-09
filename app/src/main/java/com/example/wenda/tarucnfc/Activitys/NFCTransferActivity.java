package com.example.wenda.tarucnfc.Activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;

public class NFCTransferActivity extends BaseActivity implements NfcAdapter.CreateNdefMessageCallback{

    private String mTransferAccountID;
    private String mAmount;
    private Spinner mSpinnerTopUpAmount;
    private Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctransfer);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_transfer_balance);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSpinnerTopUpAmount = (Spinner) findViewById(R.id.spinner_topup_amount);

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

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            Intent intent = new Intent(NFCTransferActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void ConfirmButton(View view) throws InvalidInputException {
        mTransferAccountID = getLoginDetail(this).getAccountID();

        try {

            account.verifyAccountBalance(mSpinnerTopUpAmount.getSelectedItem().toString());

            if (isNetworkAvailable(this)) {
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.title7) .content(R.string.content)
                        .positiveText(R.string.agree).negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() { @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            shortToast(NFCTransferActivity.this, "Transfer Successful.");
                            finish();
                            Intent intent = new Intent(NFCTransferActivity.this, MainActivity.class);
                            startActivity(intent);
                        }})
                        .show();
            } else {
                shortToast(this, "Network not available.");
            }

        } catch (InvalidInputException ex) {
            shortToast(this, ex.getInfo());
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        mTransferAccountID = getLoginDetail(this).getAccountID();
        mAmount = mSpinnerTopUpAmount.getSelectedItem().toString();

        String accountIDAmount = mTransferAccountID + mAmount;
        String message = accountIDAmount;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
