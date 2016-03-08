package com.example.wenda.tarucnfc.Fragments;


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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddNewFoodMenuFragment extends Fragment implements View.OnClickListener {

    private EditText mEditTextFoodName, mEditTextFoodDescription, mEditTextFoodPrice;
    private Spinner mSpinnerFoodCategory;
    private Button mButtonConfirm;
    private ImageView mFoodPicture = null;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private Bitmap mBitmapFoodPicture;
    private FoodMenu foodMenu = new FoodMenu();
    private final static String Add_NEW_FOOD_URL = "";

    public AddNewFoodMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_food_menu, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        return view;
    }

    private void setFindviewbyid(View view) {
        bottomSheetLayout = (BottomSheetLayout) view.findViewById(R.id.bottomSheetLayout);
        mEditTextFoodName = (EditText) view.findViewById(R.id.editText_foodName);
        mEditTextFoodDescription = (EditText) view.findViewById(R.id.editText_foodDescription);
        mEditTextFoodPrice = (EditText) view.findViewById(R.id.editText_foodPrice);
        mSpinnerFoodCategory = (Spinner) view.findViewById(R.id.spinner_food_category);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
        mFoodPicture = (ImageView) view.findViewById(R.id.food_picture);
        mFoodPicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.food_picture:
                showSheetView();
                break;

            case R.id.button_confirm:
                // verify data
                verifyData();
                // add data
                addData();
                break;

            default:
                break;
        }
    }

    private void addData() {
        foodMenu.setFoodName(mEditTextFoodName.getText().toString());
        foodMenu.setFoodDescription(mEditTextFoodDescription.getText().toString());
        foodMenu.setFoodCategory(mSpinnerFoodCategory.getSelectedItem().toString());
        foodMenu.setFoodPrice(mEditTextFoodPrice.getText().toString());
        foodMenu.setFoodPictureBitmap(mBitmapFoodPicture);

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            new AddFoodMenu(foodMenu).execute();
            clearData();
            BaseActivity.shortToast(getActivity(), "Food Item Created.");
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    public class AddFoodMenu extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        FoodMenu foodMenu;

        public AddFoodMenu(FoodMenu foodMenu) {
            this.foodMenu = foodMenu;
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
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("foodPicture", new BaseActivity().getStringImage(this.foodMenu.getFoodPictureBitmap()));
            data.put("foodPicturePath", "");
            data.put("accountID", this.foodMenu.getFoodName());
            data.put("name", this.foodMenu.getFoodDescription());
            data.put("NRICNo", this.foodMenu.getFoodCategory());
            data.put("contactNo", this.foodMenu.getFoodPrice());

            return requestHandler.sendPostRequest(Add_NEW_FOOD_URL, data);
        }
    }

    private void clearData() {
        mEditTextFoodPrice.setText("");
        mEditTextFoodDescription.setText("");
        mEditTextFoodName.setText("");
        mSpinnerFoodCategory.setSelection(0);
        mFoodPicture.setImageResource(R.drawable.ic_food_fork_drink);
    }

    private void verifyData() {
        try {
            foodMenu.verifyFoodName(mEditTextFoodName.getText().toString());
            foodMenu.verifyFoodDescription(mEditTextFoodDescription.getText().toString());
        } catch (InvalidInputException e) {
            new BaseActivity().shortToast(getActivity(), e.getInfo());
        }
    }

    // pop up dialog allow user select picture from gallery or capture photo
    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(getActivity())
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(getActivity())
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
                            startActivityForResult(createPickIntent(), new BaseActivity().REQUEST_LOAD_IMAGE);
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
        if (picImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                startActivityForResult(takePictureIntent, new BaseActivity().REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the File
                new BaseActivity().genericError("Could not create imageFile for camera");
            }
        }
    }

    @Nullable
    public Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    private void showSelectedImage(Uri selectedImageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            mFoodPicture.setImageDrawable(null);
            mFoodPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mFoodPicture.setImageBitmap(scaled);
            mBitmapFoodPicture = scaled;
            //account.setProfilePictureBitmap(mBitmapProfilePicture);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void errorMessage(String message) {
        Toast.makeText(getActivity(), message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            if (requestCode == new BaseActivity().REQUEST_LOAD_IMAGE && data != null) {
                selectedImage = data.getData();
                if (selectedImage == null) {
                    //genericError();
                }
            } else if (requestCode == new BaseActivity().REQUEST_IMAGE_CAPTURE) {
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
