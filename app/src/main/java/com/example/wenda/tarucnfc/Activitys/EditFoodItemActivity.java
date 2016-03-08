package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodMenuContract;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditFoodItemActivity extends BaseActivity implements View.OnClickListener {

    private String FoodMenuID, FoodStallID;
    private FoodMenu foodMenu = new FoodMenu();
    private ImageView mImageViewFood;
    private Button mButtonUpdate;
    private EditText mEditTextFoodDescription, mEditTextFoodPrice;
    private TextView mTextViewFoodName;
    private Spinner mSpinnerFoodCategory;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private Bitmap mBitmapFoodPicture;
    final static String GET_FOOD_URL = "http://fypproject.host56.com/FoodOrder/get_food_menu1.php";
    private final static String UPDATE_FOOD_URL = "http://fypproject.host56.com/FoodOrder/update_food_item.php";

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
        mTextViewFoodName = (TextView) findViewById(R.id.text_view_foodName);
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
                // verify data
                verifyData();
                break;

            case R.id.food_picture:
                showSheetView();
                break;

            default:
                break;
        }
    }

    private void verifyData() {
        try {
            foodMenu.verifyFoodDescription(mEditTextFoodDescription.getText().toString());
            foodMenu.verifyFoodPrice(mEditTextFoodPrice.getText().toString());
            // add data
            addData();
        } catch (InvalidInputException e) {
            shortToast(this, e.getInfo());
        }
    }

    private void addData() {
        foodMenu.setFoodName(mTextViewFoodName.getText().toString());
        foodMenu.setFoodDescription(mEditTextFoodDescription.getText().toString());
        foodMenu.setFoodCategory(mSpinnerFoodCategory.getSelectedItem().toString());
        foodMenu.setFoodPrice(mEditTextFoodPrice.getText().toString());
        foodMenu.setFoodPictureBitmap(mBitmapFoodPicture);

        // check network
        if(isNetworkAvailable(this) == true) {
            new UpdateFood(FoodMenuID, foodMenu).execute();
        } else {
            shortToast(this, "Network not available");
        }
    }

    public class UpdateFood extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String foodMenuID, foodStallID;
        FoodMenu foodMenu;

        public UpdateFood(String foodMenuID, FoodMenu foodMenu) {
            this.foodMenuID = foodMenuID;
            this.foodMenu = foodMenu;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditFoodItemActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditFoodItemActivity.this, "OFF");
            extractJsonData1(s);
            switch (foodMenu.getResponse()){
                case 1:
                    shortToast(EditFoodItemActivity.this, "Food Item Updated.");
                    finish();
                    break;
                case 2:
                    shortToast(EditFoodItemActivity.this, "Failed to update food item.");
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("foodMenuID", foodMenuID);
            if (foodMenu.getFoodPictureBitmap() == null) {
                data.put("foodPicture", "");
                data.put("foodPicturePath", this.foodMenu.getFoodMenuImagePath());
            } else {
                data.put("foodPicture", getStringImage(this.foodMenu.getFoodPictureBitmap()));
                data.put("foodPicturePath", "");
            }
            data.put("foodName", this.foodMenu.getFoodName());
            data.put("foodDescription", this.foodMenu.getFoodDescription());
            data.put("foodCategory", this.foodMenu.getFoodCategory());
            data.put("foodPrice", this.foodMenu.getFoodPrice());

            return requestHandler.sendPostRequest(UPDATE_FOOD_URL, data);
        }
    }

    private void extractJsonData1(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            foodMenu.setResponse(jsonObject.getInt(KEY_RESPONSE));
            Log.d("track", " " +foodMenu.getResponse());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
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

        mTextViewFoodName.setText(foodMenu.getFoodName());
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
        mEditTextFoodPrice.setText(foodMenu.getFoodPrice());
        ImageLoader.getInstance().displayImage(foodMenu.getFoodMenuImagePath(), mImageViewFood, options);
    }

    // pop up dialog allow user select picture from gallery or capture photo
    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(EditFoodItemActivity.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isCameraTile()) {
                            dispatchTakePictureIntent();
                        } else if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);
                        } else if (selectedTile.isImageTile()) {
                            showSelectedImage(selectedTile.getImageUri());
                        } else {
                            //genericError();
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        Log.d("track", "picture ");


        bottomSheetLayout.showWithSheetView(sheetView);
    }

    @Nullable
    public Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(this.getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    /**
     * For images captured from the camera, we need to create a File first to tell the camera
     * where to store the image.
     *
     * @return the File created for the image to be store under.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    /**
     * This utility function combines the camera intent creation and image file creation, and
     * ultimately fires the intent.
     *
     * @see {@link #createCameraIntent()}
     * @see {@link #createImageFile()}
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = createCameraIntent();
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent != null) {
            // Create the File where the photo should go
            try {
                File imageFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the File
                genericError("Could not create imageFile for camera");
            }
        }
    }

    @Nullable
    public Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    private void showSelectedImage(Uri selectedImageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            mImageViewFood.setImageDrawable(null);
            mImageViewFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViewFood.setImageBitmap(scaled);
            mBitmapFoodPicture = scaled;
            //account.setProfilePictureBitmap(mBitmapProfilePicture);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void errorMessage(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
                selectedImage = data.getData();
                if (selectedImage == null) {
                    //genericError();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                selectedImage = cameraImageUri;
            }

            if (selectedImage != null) {
                showSelectedImage(selectedImage);
            } else {
                //genericError();
            }
        }
    }
}
