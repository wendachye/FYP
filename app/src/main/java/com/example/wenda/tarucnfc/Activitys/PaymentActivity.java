package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import java.util.Calendar;
import java.util.HashMap;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    private String selectType, totalPrice, gstPrice, accountID;
    private TextView mTextViewTotalPrice;
    private Button mButtonConfirm, mButtonCancel;
    private final static String CONFIRM_PAYMENT_URL = "http://fypproject.host56.com/FoodOrder/confirm_payment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // set current date time
        calendar = Calendar.getInstance();

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_payment);

        selectType = getIntent().getStringExtra("selected");

        if (selectType.equals("direct_payment")){
            totalPrice = getIntent().getStringExtra("totalPrice");
            gstPrice = getIntent().getStringExtra("gstPrice");
        }

        mTextViewTotalPrice = (TextView) findViewById(R.id.totalAmount);
        mButtonConfirm = (Button) findViewById(R.id.button_confirm);
        mButtonCancel = (Button) findViewById(R.id.button_cancel);
        mButtonConfirm.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);

        mTextViewTotalPrice.setText("RM " + totalPrice);
        accountID = getLoginDetail(this).getAccountID();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_cancel:
                finish();
                break;

            case R.id.button_confirm:
                // check network
                if(isNetworkAvailable(this) == true) {
                    new ConfirmPayment(accountID, totalPrice, gstPrice).execute();
                } else {
                    shortToast(this, "Network not available");
                }
                break;

            default:
                break;
        }
    }

    public class ConfirmPayment extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String accountID, totalPrice, gstPrice;

        public ConfirmPayment(String accountID, String totalPrice, String gstPrice) {
            this.accountID = accountID;
            this.totalPrice = totalPrice;
            this.gstPrice = gstPrice;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(PaymentActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(PaymentActivity.this, "OFF");
            shortToast(PaymentActivity.this, "Payment done.");
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", accountID);
            data.put("totalPrice", totalPrice);
            data.put("gstPrice", gstPrice);
            data.put("dateTime", dateTimeFormat.format(calendar.getTime()));

            return requestHandler.sendPostRequest(CONFIRM_PAYMENT_URL, data);
        }
    }
}
