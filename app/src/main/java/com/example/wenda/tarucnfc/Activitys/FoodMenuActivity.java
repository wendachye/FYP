package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Adapter.AdapterFoodMenu;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodMenuContract;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodMenuActivity extends BaseActivity implements AdapterFoodMenu.AdapterCallBack, View.OnClickListener {

    private String foodStallID;
    private RecyclerView mRecyclerView;
    private JSONArray mJsonArray;
    private SwipeRefreshLayout mSwipeContainer;
    private AdapterFoodMenu adapterFoodMenu;
    private LinearLayout mLinearLayoutNoRecord;
    private ArrayList<FoodMenu> mListFoodMenu = new ArrayList<>();
    final static String GET_FOOD_MENU_URL = "http://fypproject.host56.com/FoodOrder/get_food_menu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_foodmenu);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodStallID = getIntent().getStringExtra("FoodStallID");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutNoRecord = (LinearLayout) findViewById(R.id.layout_no_record);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        if (isNetworkAvailable(this)) {
            mListFoodMenu.clear();
            new GetJson(foodStallID).execute();
        } else {
            shortToast(this, "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(FoodMenuActivity.this)) {
                    mListFoodMenu.clear();
                    new GetJson(foodStallID).execute();
                } else {
                    shortToast(FoodMenuActivity.this, "Network not available, couldn't refresh.");
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
    public void adapterOnClick(int adapterPosition) {

    }

    @Override
    public void onClick(View view) {

    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {

        String foodStallID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String foodStallID) {
            this.foodStallID = foodStallID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(FoodMenuActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(FoodMenuActivity.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("foodStallID", foodStallID);
            return rh.sendPostRequest(GET_FOOD_MENU_URL, data);
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
                FoodMenu foodMenu = new FoodMenu();

                foodMenu.setFoodMenuID(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_MENU_ID));
                foodMenu.setFoodName(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_NAME));
                foodMenu.setFoodCategory(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_CATEGORY));
                foodMenu.setFoodPrice(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_PRICE));
                foodMenu.setFoodMenuImagePath(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_MENU_IMAGE_PATH));

                mListFoodMenu.add(foodMenu);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }

        if(mJsonArray.length() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterFoodMenu = new AdapterFoodMenu(this, mListFoodMenu, R.layout.row_food_menu, this);
            mRecyclerView.setAdapter(adapterFoodMenu);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }
}
