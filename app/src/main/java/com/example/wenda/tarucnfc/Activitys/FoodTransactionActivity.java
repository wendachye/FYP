package com.example.wenda.tarucnfc.Activitys;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wenda.tarucnfc.Adapter.AdapterFoodTransaction;
import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodTransactionActivity extends BaseActivity {

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    private AdapterFoodTransaction adapterFoodTransaction;
    private LinearLayout mLinearLayoutNoRecord;
    private ArrayList<FoodOrder> mListFoodTransaction = new ArrayList<>();
    private JSONArray mJsonArray;
    private static final String GET_FOOD_TRANSACTION_URL = "http://fypproject.host56.com/FoodOrder/get_food_transaction.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_transaction);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_food_transaction);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutNoRecord = (LinearLayout) findViewById(R.id.layout_no_record);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        if (isNetworkAvailable(this)) {
            mListFoodTransaction.clear();
            new GetJson(getLoginDetail(this).getAccountID()).execute();
        } else {
            shortToast(this, "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(FoodTransactionActivity.this)) {
                    mListFoodTransaction.clear();
                    new GetJson(getLoginDetail(FoodTransactionActivity.this).getAccountID()).execute();
                } else {
                    shortToast(FoodTransactionActivity.this, "Network not available, couldn't refresh.");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });
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
            UIUtils.getProgressDialog(FoodTransactionActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(FoodTransactionActivity.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, accountID);
            return rh.sendPostRequest(GET_FOOD_TRANSACTION_URL, data);
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
                FoodOrder foodOrder = new FoodOrder();

                foodOrder.setFoodOrderID(jsonObject.getString("FoodTransactionID"));
                foodOrder.setPaymentDateTime(jsonObject.getString("PaymentDateTime"));
                foodOrder.setTotalPrice(jsonObject.getString("PaymentAmount"));
                foodOrder.setStatus(jsonObject.getString("PaymentStatus"));

                mListFoodTransaction.add(foodOrder);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        if (mListFoodTransaction.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterFoodTransaction = new AdapterFoodTransaction(this, mListFoodTransaction  , R.layout.row_food_transaction);
            mRecyclerView.setAdapter(adapterFoodTransaction);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }
}
