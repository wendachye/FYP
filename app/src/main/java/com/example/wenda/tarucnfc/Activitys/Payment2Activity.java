package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class Payment2Activity extends BaseActivity implements View.OnClickListener{

    private String selectType, totalPrice, gstPrice, accountID;
    private TextView mTextViewTotalPrice;
    private Button mButtonConfirm, mButtonCancel;
    private OfflineLogin offlineLogin = new OfflineLogin();
    private final static String CONFIRM_PAYMENT_URL = "http://fypproject.host56.com/FoodOrder/confirm_payment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);

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

                new ConfirmPayment(accountID, totalPrice, gstPrice).execute();
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.title7) .content(R.string.content)
                        .positiveText(R.string.agree).negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() { @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            shortToast(Payment2Activity.this, "Payment Successful.");
                            finish();
                        }})
                        .show();

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
            //UIUtils.getProgressDialog(Payment2Activity.this, "ON");

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //UIUtils.getProgressDialog(Payment2Activity.this, "OFF");
            extractJsonData(s);
            switch (offlineLogin.getLoginResponse()){
                // 1 = success
                case 1:
                    //shortToast(Payment2Activity.this, "Payment done.");
                    //finish();
                    break;

                // 2 = account balance insufficient
                case 2:
                    shortToast(Payment2Activity.this, "Account Balance Insufficient. Please top up your wallet.");
                    break;
            }
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

    private void extractJsonData(String json) {
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            offlineLogin.setLoginResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
