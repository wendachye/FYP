package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Adapter.AdapterOrderCart;
import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends BaseActivity implements View.OnClickListener{

    private String accountID;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeContainer;
    private TextView mTextViewFoodTotalPrice, mTextViewGSTPrice, mTextViewGrandTotalPrice;
    private Button mButtonCheckout, mButtonCancel;
    private LinearLayout mLinearLayoutNoRecord;
    private FoodOrder foodOrder = new FoodOrder();
    private JSONArray mJsonArray;
    private ArrayList<FoodOrder> mListFoodOrder = new ArrayList<>();
    private AdapterOrderCart adapterOrderCart;
    final static String GET_FOOD_ORDER_URL = "http://fypproject.host56.com/FoodOrder/get_order_cart.php";
    final static String CLEAR_FOOD_ORDER_URL = "http://fypproject.host56.com/FoodOrder/clear_order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_order_cart);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mLinearLayoutNoRecord = (LinearLayout) findViewById(R.id.layout_no_record);
        mButtonCancel = (Button) findViewById(R.id.button_clear);
        mButtonCancel.setOnClickListener(this);
        mButtonCheckout = (Button) findViewById(R.id.button_checkout);
        mButtonCheckout.setOnClickListener(this);
        mTextViewFoodTotalPrice = (TextView) findViewById(R.id.text_view_TotalPrice);
        mTextViewGSTPrice = (TextView) findViewById(R.id.text_view_GSTPrice);
        mTextViewGrandTotalPrice = (TextView) findViewById(R.id.text_view_GrandTotal);

        accountID = getLoginDetail(this).getAccountID();

        if (isNetworkAvailable(this)) {
            mListFoodOrder.clear();
            new GetJson(accountID).execute();
        } else {
            shortToast(this, "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(CartActivity.this)) {
                    mListFoodOrder.clear();
                    new GetJson(accountID).execute();
                } else {
                    shortToast(CartActivity.this, "Network not available, couldn't refresh.");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_clear:
                new ClearData(accountID).execute();
                break;

            case R.id.button_checkout:
                Intent intent = new Intent(this, PaymentActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    // this one is get json
    public class ClearData extends AsyncTask<String, Void, String> {
        String accountID;
        RequestHandler rh = new RequestHandler();

        public ClearData(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(CartActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(CartActivity.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mTextViewGSTPrice.setText("");
            mTextViewFoodTotalPrice.setText("");
            mTextViewGrandTotalPrice.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, accountID);
            return rh.sendPostRequest(CLEAR_FOOD_ORDER_URL, data);
        }
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
            UIUtils.getProgressDialog(CartActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(CartActivity.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", accountID);
            return rh.sendPostRequest(GET_FOOD_ORDER_URL, data);
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
        double totalPrice = 0.00;
        double GSTPrice = 0.00;
        double grandTotal = 0.00;

        for (int i = 0; i < mJsonArray.length(); i++) {
            double subTotal = 0.00;
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                FoodOrder foodOrder = new FoodOrder();

                foodOrder.setFoodName(jsonObject.getString("FoodName"));
                foodOrder.setItemQuantity(jsonObject.getString("Quantity"));
                foodOrder.setFoodPrice(jsonObject.getString("FoodPrice"));
                subTotal = Double.parseDouble(foodOrder.getFoodPrice()) * Integer.parseInt(foodOrder.getItemQuantity());
                foodOrder.setSubTotal(String.format("%.2f", subTotal));

                mListFoodOrder.add(foodOrder);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }

            totalPrice += subTotal;
        }

        GSTPrice = totalPrice * 6 / 100;
        grandTotal = GSTPrice + totalPrice;

        if (mJsonArray.length() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mTextViewFoodTotalPrice.setText("RM " + String.format("%.2f", totalPrice));
            mTextViewGSTPrice.setText("RM " + String.format("%.2f", GSTPrice));
            mTextViewGrandTotalPrice.setText("RM " + String.format("%.2f", grandTotal));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterOrderCart = new AdapterOrderCart(this, mListFoodOrder, R.layout.row_cart);
            mRecyclerView.setAdapter(adapterOrderCart);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }
}
