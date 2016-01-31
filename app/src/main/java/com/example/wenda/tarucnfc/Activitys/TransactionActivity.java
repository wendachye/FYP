package com.example.wenda.tarucnfc.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.wenda.tarucnfc.AdapterTransaction;
import com.example.wenda.tarucnfc.Databases.Contracts.TransactionContract.TransactionRecord;
import com.example.wenda.tarucnfc.Domains.Transaction;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionActivity extends BaseActivity {

    AdapterTransaction adapterTransaction;
    RecyclerView mRecyclerView;
    private ArrayList<Transaction> mListTransaction = new ArrayList<>();
    private static final String GET_TRANSACTION_URL = "http://tarucandroid.comxa.com/Wallet/get_transaction_history.php";
    private JSONArray mJsonArray;
    private Transaction transaction = new Transaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.transaction_title);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetJson(getLoginDetail(this).getAccountID()).execute();

        adapterTransaction = new AdapterTransaction(this, mListTransaction, R.layout.row_transaction_history);
        mRecyclerView.setAdapter(adapterTransaction);
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

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(TransactionActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(TransactionActivity.this, "OFF");
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, accountID);
            return rh.sendPostRequest(GET_TRANSACTION_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractJsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);

                transaction.setSenderID(jsonObject.getString(TransactionRecord.COLUMN_SENDER_ID));
                transaction.setRecipientID(jsonObject.getString(TransactionRecord.COLUMN_RECIPIENT_ID));
                transaction.setTransactionType(jsonObject.getString(TransactionRecord.COLUMN_TRANSACTION_TYPE));
                transaction.setAmount(jsonObject.getString(TransactionRecord.COLUMN_Amount));
                transaction.setDateTime(jsonObject.getString(TransactionRecord.COLUMN_DATE_TIME));
                transaction.setStatus(jsonObject.getString(TransactionRecord.COLUMN_STATUS));
                transaction.setRemark(jsonObject.getString(TransactionRecord.COLUMN_REMARK));

                mListTransaction.add(transaction);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
    }
}