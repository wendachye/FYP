package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodStallContract;
import com.example.wenda.tarucnfc.Domains.FoodStall;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class FoodStallDetailsFragment extends Fragment implements View.OnClickListener{

    private EditText mEditTextStallName;
    private Button mButtonEdit;
    private CardView mCardViewEditFoodStall;
    private TextView mTextViewStallName;
    private TextView mTextViewOwnerID;
    private TextView mTextViewJoinedDate;
    private ImageView mImageViewFood;
    private TextView mTextViewStatus;

    private FoodStall foodStall = new FoodStall();
    private static final String KEY_RESPONSE = "Response";
    private static final String SEARCH_FOOD_STALL_URL = "http://fypproject.host56.com/FoodOrder/search_food_stall1.php";


    public FoodStallDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_stall_details, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        new searchFoodStall(new BaseActivity().getLoginDetail(getActivity()).getAccountID()).execute();

        return view;
    }

    private void setFindviewbyid(View view) {
        mEditTextStallName = (EditText) view.findViewById(R.id.editText_stallName);
        mButtonEdit = (Button) view.findViewById(R.id.button_edit);
        mButtonEdit.setOnClickListener(this);
        mImageViewFood = (ImageView) view.findViewById(R.id.stall_picture);
        mTextViewStallName = (TextView) view.findViewById(R.id.text_view_stallName);
        mTextViewOwnerID = (TextView) view.findViewById(R.id.text_view_ownerID);
        mTextViewJoinedDate = (TextView) view.findViewById(R.id.text_view_joinedDate);
        mTextViewStatus = (TextView) view.findViewById(R.id.text_view_status);
        mCardViewEditFoodStall = (CardView) view.findViewById(R.id.edit_food_stall);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_search:
                if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
                    mCardViewEditFoodStall.setVisibility(View.GONE);
                    //new searchFoodStall(mEditTextStallName.getText().toString()).execute();
                } else {
                    BaseActivity.shortToast(getActivity(), "Network not available");
                }
                break;

            default:
                break;
        }
    }

    // this is get json
    public class searchFoodStall extends AsyncTask<String, Void, String> {

        String accountID;
        RequestHandler rh = new RequestHandler();

        public searchFoodStall(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(getActivity(), "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(getActivity(), "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", accountID);
            return rh.sendPostRequest(SEARCH_FOOD_STALL_URL, data);
        }
    }

    private void extractJsonData(String json) {
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            foodStall.setFoodStallID(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_FOOD_STALL_ID));
            foodStall.setAccountID(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_ACCOUNT_ID));
            foodStall.setStallName(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_STALL_NAME));
            foodStall.setJoinedDate(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_JOINED_DATE));
            foodStall.setStatus(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_STATUS));
            foodStall.setFoodStallImagePath(jsonObject.getString("FoodStallImagePath"));
            foodStall.setResponse(jsonObject.getInt(KEY_RESPONSE));

            initialValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialValues() {
        mTextViewStallName.setText(foodStall.getStallName());
        mTextViewOwnerID.setText(foodStall.getAccountID());
        mTextViewJoinedDate.setText(foodStall.getJoinedDate());
        mTextViewStatus.setText(foodStall.getStatus());
        ImageLoader.getInstance().displayImage(foodStall.getFoodStallImagePath(), mImageViewFood, new BaseActivity().options);
    }

}
