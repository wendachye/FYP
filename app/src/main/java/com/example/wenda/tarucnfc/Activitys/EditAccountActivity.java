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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Domains.Account;
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

public class EditAccountActivity extends BaseActivity implements View.OnClickListener {

    private EditText mTextFullName;
    private EditText mTextNRICNO;
    private Spinner mSpinnerGender;
    private EditText mTextEmail;
    private EditText mTextContactNo;
    private EditText mTextHomeAddress;
    private EditText mTextCampusAddress;
    private ImageView mProfilePicture = null;
    private LinearLayout mCampusAddress;

    private final static String GET_JSON_URL = "http://fypproject.host56.com/Account/edit_account_view.php";
    private final static String UPDATE_ACCOUNT_URL = "http://fypproject.host56.com/Account/update_account.php";
    private String mAccountID;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private Account account = new Account();
    private Bitmap mBitmapProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.edit_account_title);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccountID = getLoginDetail(this).getAccountID();

        setFindviewbyid();

        new GetJson(String.valueOf(mAccountID)).execute();
    }

    public void setFindviewbyid() {
        mCampusAddress = (LinearLayout) findViewById(R.id.linear_layout_campus);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        mProfilePicture = (ImageView) findViewById(R.id.profile_picture);
        mProfilePicture.setOnClickListener(this);
        mTextFullName = (EditText) findViewById(R.id.edit_text_fullname);
        mTextNRICNO = (EditText) findViewById(R.id.edit_text_nric);
        mSpinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        mTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mTextContactNo = (EditText) findViewById(R.id.edit_text_contact);
        mTextHomeAddress = (EditText) findViewById(R.id.edit_text_homeAddress);
        mTextCampusAddress = (EditText) findViewById(R.id.edit_text_campusAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_button, menu);
        return true;
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
        else if (id == R.id.saveButton) {
            updateAccount();
            finish();
            shortToast(this, "Profile Updated.");
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
            UIUtils.getProgressDialog(EditAccountActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(EditAccountActivity.this, "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", String.valueOf(mAccountID));
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setAccountID(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
            Log.d("track", "get id *" + jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
            account.setName(jsonObject.getString(AccountContract.AccountRecord.KEY_NAME));
            account.setNRICNo(jsonObject.getString(AccountContract.AccountRecord.KEY_NRIC_NO));
            account.setContactNo(jsonObject.getString(AccountContract.AccountRecord.KEY_CONTACT_NO));
            account.setEmailAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_EMAIL_ADDRESS));
            account.setGender(jsonObject.getString(AccountContract.AccountRecord.KEY_GENDER));
            account.setHomeAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_HOME_ADDRESS));
            account.setCampusAddress(jsonObject.getString(AccountContract.AccountRecord.KEY_CAMPUS_ADDRESS));
            account.setAccountType(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_TYPE));
            account.setAccountBalance(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_BALANCE));
            account.setPINcode(jsonObject.getString(AccountContract.AccountRecord.KEY_PIN_CODE));
            account.setStatus(jsonObject.getString(AccountContract.AccountRecord.KEY_STATUS));
            account.setProfilePicturePath(jsonObject.getString(AccountContract.AccountRecord.KEY_PROFILE_PICTURE_PATH));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {
        mTextFullName.setText(account.getName());
        mTextNRICNO.setText(account.getNRICNo());
        if (account.getGender().equals("Male")) {
            mSpinnerGender.setSelection(0);
        } else {
            mSpinnerGender.setSelection(1);
        }
        mTextEmail.setText(account.getEmailAddress());
        mTextContactNo.setText(account.getContactNo());
        mTextHomeAddress.setText(account.getHomeAddress());
        if (account.getAccountType().equals("Back End")) {
            mCampusAddress.setVisibility(View.GONE);
        } else {
            mCampusAddress.setVisibility(View.VISIBLE);
            mTextCampusAddress.setText(account.getCampusAddress());
        }
        ImageLoader.getInstance().displayImage(account.getProfilePicturePath(), mProfilePicture, options);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_picture:
                showSheetView();
                break;

            default:
                break;
        }
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
                        Glide.with(EditAccountActivity.this)
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

    private void showSelectedImage(Uri selectedImageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            mProfilePicture.setImageDrawable(null);
            mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mProfilePicture.setImageBitmap(scaled);
            mBitmapProfilePicture = scaled;
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


    private void updateAccount() {

        // set all the related values into account domain
        account.setProfilePictureBitmap(mBitmapProfilePicture);
        account.setName(mTextFullName.getText().toString());
        account.setNRICNo(mTextNRICNO.getText().toString());
        account.setContactNo(mTextContactNo.getText().toString());
        account.setEmailAddress(mTextEmail.getText().toString());
        account.setGender(mSpinnerGender.getSelectedItem().toString());
        account.setHomeAddress(mTextHomeAddress.getText().toString());
        account.setCampusAddress(mTextCampusAddress.getText().toString());

        // check network
        if(isNetworkAvailable(this) == true) {
            new UpdateAccount(account).execute();
        } else {
            shortToast(this, "Network not available");
        }
    }

    public class UpdateAccount extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        Account account;

        public UpdateAccount(Account account) {
            this.account = account;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditAccountActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditAccountActivity.this, "OFF");
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            if (account.getProfilePictureBitmap() == null) {
                data.put("profilePicture", "");
                data.put("profilePicturePath", this.account.getProfilePicturePath());
                Log.d("track", "path " + this.account.getProfilePicturePath());
            } else {
                data.put("profilePicture", getStringImage(this.account.getProfilePictureBitmap()));
                data.put("profilePicturePath", "");
            }
            data.put("accountID", mAccountID);
            Log.d("track", "accountID " + mAccountID);
            data.put("name", this.account.getName());
            data.put("NRICNo", this.account.getNRICNo());
            data.put("contactNo", this.account.getContactNo());
            data.put("emailAddress", this.account.getEmailAddress());
            data.put("gender", this.account.getGender());
            data.put("homeAddress", this.account.getHomeAddress());
            data.put("campusAddress", this.account.getCampusAddress());

            return requestHandler.sendPostRequest(UPDATE_ACCOUNT_URL, data);
        }
    }

}
