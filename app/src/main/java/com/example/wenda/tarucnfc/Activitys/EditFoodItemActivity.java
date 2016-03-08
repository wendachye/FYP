package com.example.wenda.tarucnfc.Activitys;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.wenda.tarucnfc.Databases.Contracts.FoodMenuContract;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditFoodItemActivity extends BaseActivity implements View.OnClickListener {

    private String FoodMenuID;
    private FoodMenu foodMenu = new FoodMenu();
    private ImageView mImageViewFood;
    private Button mButtonUpdate;
    private EditText mEditTextFoodName, mEditTextFoodDescription, mEditTextFoodPrice;
    private Spinner mSpinnerFoodCategory;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private Bitmap mBitmapFoodPicture;
    final static String GET_FOOD_URL = "http://fypproject.host56.com/FoodOrder/get_food_menu1.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_item);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_edit_food);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set item findviewbyid
        mButtonUpdate = (Button) findViewById(R.id.button_update);
        mButtonUpdate.setOnClickListener(this);
        mImageViewFood = (ImageView) findViewById(R.id.food_picture);
        mImageViewFood.setOnClickListener(this);
        mEditTextFoodName = (EditText) findViewById(R.id.editText_foodName);
        mEditTextFoodDescription = (EditText) findViewById(R.id.editText_foodDescription);
        mEditTextFoodPrice = (EditText) findViewById(R.id.editText_foodPrice);
        mSpinnerFoodCategory = (Spinner) findViewById(R.id.spinner_food_category);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);

        FoodMenuID = getIntent().getStringExtra("FoodMenuID");

        if (isNetworkAvailable(this)) {
            new GetJson(FoodMenuID).execute();
        } else {
            shortToast(this, "Network not available.");
        }

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
            case R.id.button_update:

                break;

            default:
                break;
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
            UIUtils.getProgressDialog(EditFoodItemActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(EditFoodItemActivity.this, "OFF");
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

        mEditTextFoodName.setText(foodMenu.getFoodName());
        switch (foodMenu.getFoodCategory()){
            case "Food":
                mSpinnerFoodCategory.setSelection(0);
                break;
            case "Beverage":
                mSpinnerFoodCategory.setSelection(1);
                break;
            case "Dessert":
                mSpinnerFoodCategory.setSelection(2);
                break;
            case "Snack":
                mSpinnerFoodCategory.setSelection(3);
                break;
            default:
                break;
        }
        mEditTextFoodDescription.setText(foodMenu.getFoodDescription());
        mEditTextFoodPrice.setText("RM " + foodMenu.getFoodPrice());
        ImageLoader.getInstance().displayImage(foodMenu.getFoodMenuImagePath(), mImageViewFood, options);
    }
}
