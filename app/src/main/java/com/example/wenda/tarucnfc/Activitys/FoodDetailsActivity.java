package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodMenuContract;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FoodDetailsActivity extends BaseActivity implements View.OnClickListener {

    private String FoodMenuID;
    private String quantity;
    private FoodMenu foodMenu = new FoodMenu();
    private SwipeRefreshLayout mSwipeContainer;
    private TextView mTextViewFoodName, mTextViewFoodCategory, mTextViewFoodDescription, mTextViewFoodPrice;
    private ImageView mImageViewFood;
    private FloatingActionButton mFabAdd;
    final static String GET_FOOD_URL = "http://fypproject.host56.com/FoodOrder/get_food_menu1.php";
    final static String ADD_FOOD_ORDER_URL = "http://fypproject.host56.com/FoodOrder/add_food_transaction.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_food_details);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextViewFoodName = (TextView) findViewById(R.id.text_view_foodName);
        mTextViewFoodDescription = (TextView) findViewById(R.id.text_view_foodDescription);
        mTextViewFoodCategory = (TextView) findViewById(R.id.text_view_foodCategory);
        mTextViewFoodPrice = (TextView) findViewById(R.id.text_view_foodPrice);
        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mImageViewFood = (ImageView) findViewById(R.id.image_food);
        mFabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(this);

        FoodMenuID = getIntent().getStringExtra("FoodMenuID");

        if (isNetworkAvailable(this)) {
            new GetJson(FoodMenuID).execute();
        } else {
            shortToast(this, "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(FoodDetailsActivity.this)) {
                    new GetJson(FoodMenuID).execute();
                } else {
                    shortToast(FoodDetailsActivity.this, "Network not available, couldn't refresh.");
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
            case R.id.fab_add:
                // display dialog box
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.quantity)
                        .customView(R.layout.dialog_custom_view, true)
                        .positiveText(R.string.confirm)
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                shortToast(FoodDetailsActivity.this, "Added to cart.");
                                // run php to add order line
                                String accountID;
                                accountID = getLoginDetail(FoodDetailsActivity.this).getAccountID();
                                new addOrder(accountID, FoodMenuID, quantity).execute();
                                finish();
                            }
                        }).build();

                final Spinner mSpinner = (Spinner) dialog.getCustomView().findViewById(R.id.spinner_quantity);
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (mSpinner.getSelectedItemPosition()){
                            case 0:
                                quantity = "1";
                                break;

                            case 1:
                                quantity = "2";
                                break;

                            case 2:
                                quantity = "3";
                                break;

                            case 3:
                                quantity = "4";
                                break;

                            case 4:
                                quantity = "5";
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                mSpinner.setSelection(0);
                dialog.show();
                break;

            default:
                break;
        }
    }

    public class addOrder extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String accountID, foodMenuID, quantity;

        public addOrder(String accountID, String foodMenuID, String quantity) {
            this.accountID = accountID;
            this.foodMenuID = foodMenuID;
            this.quantity = quantity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(getActivity(), "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //UIUtils.getProgressDialog(getActivity(), "OFF");
            //Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", accountID);
            data.put("foodMenuID", foodMenuID);
            data.put("quantity", quantity);

            return requestHandler.sendPostRequest(ADD_FOOD_ORDER_URL, data);
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String foodMenuID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String foodMenuID) {
            this.foodMenuID = foodMenuID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(FoodDetailsActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(FoodDetailsActivity.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("foodMenuID", foodMenuID);
            return rh.sendPostRequest(GET_FOOD_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            foodMenu.setFoodName(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_NAME));
            foodMenu.setFoodCategory(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_CATEGORY));
            foodMenu.setFoodDescription(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_DESCRIPTION));
            foodMenu.setFoodPrice(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_PRICE));
            foodMenu.setFoodMenuImagePath(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_MENU_IMAGE_PATH));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {

        mTextViewFoodName.setText(foodMenu.getFoodName());
        mTextViewFoodCategory.setText(foodMenu.getFoodCategory());
        mTextViewFoodDescription.setText(foodMenu.getFoodDescription());
        mTextViewFoodPrice.setText("RM " + foodMenu.getFoodPrice());
        ImageLoader.getInstance().displayImage(foodMenu.getFoodMenuImagePath(), mImageViewFood, options);
    }
}
