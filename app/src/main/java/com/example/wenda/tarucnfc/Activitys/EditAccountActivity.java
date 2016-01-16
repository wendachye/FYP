package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.R;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditAccountActivity extends BaseActivity {

    Spinner mspinner;
    TextView mTextStudentID;
    TextView mTextProgramme;
    TextView mTextFaculty;
    TextView mTextCampus;
    TextView mTextSchoolEmail;
    TextView mTextSessionJoined;
    EditText mTextFullName;
    EditText mTextNRICNO;
    Spinner mTextGender;
    EditText mTextEmail;
    EditText mTextContactNo;
    EditText mTextHomeAddress;
    EditText mTextCampusAddress;
    ImageView mProfilePicture = null;

    private int mLoadImageNo;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private Bitmap mBitmapProfilePicture;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        mspinner = (Spinner) findViewById(R.id.spinner_gender);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.edit_account_title);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProfilePicture = (ImageView) findViewById(R.id.profile_picture);

        initialValues();
    }

    public void initialValues() {

        mTextStudentID = (TextView) findViewById(R.id.text_studentID);
        mTextProgramme = (TextView) findViewById(R.id.text_programme);
        mTextFaculty = (TextView) findViewById(R.id.text_faculty);
        mTextCampus = (TextView) findViewById(R.id.text_campus);
        mTextSchoolEmail = (TextView) findViewById(R.id.text_school_email);
        mTextSessionJoined = (TextView) findViewById(R.id.text_sessionJoined);
        mTextFullName = (EditText) findViewById(R.id.edit_text_fullname);
        mTextNRICNO = (EditText) findViewById(R.id.edit_text_nric);
        mTextGender = (Spinner) findViewById(R.id.spinner_gender);
        mTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mTextContactNo = (EditText) findViewById(R.id.edit_text_contact);
        mTextHomeAddress = (EditText) findViewById(R.id.edit_text_homeAddress);
        mTextCampusAddress = (EditText) findViewById(R.id.edit_text_campusAddress);

        // set database data to data field
        OfflineLogin offlineLogin = new BaseActivity().getLoginDetail(this);
        mTextStudentID.setText(offlineLogin.getAccountID());
        mTextProgramme.setText(offlineLogin.getProgramme());
        mTextFaculty.setText(offlineLogin.getFaculty());
        mTextCampus.setText(offlineLogin.getCampus());
        mTextSchoolEmail.setText(offlineLogin.getSchoolEmail());
        mTextSessionJoined.setText(offlineLogin.getSessionJoined());
        mTextFullName.setText(offlineLogin.getName());
        mTextNRICNO.setText(offlineLogin.getNRICNo());
        //mTextGender.set(offlineLogin.getGender());
        mTextEmail.setText(offlineLogin.getEmailAddress());
        mTextContactNo.setText(offlineLogin.getContactNo());
        mTextHomeAddress.setText(offlineLogin.getHomeAddress());
        mTextCampusAddress.setText(offlineLogin.getCampusAddress());

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

        return super.onOptionsItemSelected(item);
    }

    public void imageClick(View view) {
        showSheetView();
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

            if (mLoadImageNo == 1) {
                mProfilePicture.setImageDrawable(null);
                mProfilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                mProfilePicture.setImageBitmap(scaled);
                mBitmapProfilePicture = scaled;
                account.setProfilePictureBitmap(mBitmapProfilePicture);
            } else
                errorMessage(null);
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
