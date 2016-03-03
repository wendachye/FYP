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
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodStallContract.FoodStallRecord;
import com.example.wenda.tarucnfc.Domains.FoodStall;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SearchFoodStallFragment extends Fragment implements View.OnClickListener {

    private EditText mEditTextStallName;
    private Button mButtonSearch;
    private CardView mCardViewEditFoodStall;
    private TextView mTextViewStallName;
    private TextView mTextViewOwnerID;
    private TextView mTextViewJoinedDate;
    private TextView mTextViewStatus;

    private FoodStall foodStall = new FoodStall();
    private static final String KEY_RESPONSE = "Response";
    private static final String SEARCH_FOOD_STALL_URL = "http://fypproject.host56.com/FoodOrder/search_food_stall.php";

    public SearchFoodStallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_food_stall, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        return view;
    }

    private void setFindviewbyid(View view) {
        mEditTextStallName = (EditText) view.findViewById(R.id.editText_stallName);
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mTextViewStallName = (TextView) view.findViewById(R.id.text_view_stallName);
        mTextViewOwnerID = (TextView) view.findViewById(R.id.text_view_ownerID);
        mTextViewJoinedDate = (TextView) view.findViewById(R.id.text_view_joinedDate);
        mTextViewStatus = (TextView) view.findViewById(R.id.text_view_status);
        mCardViewEditFoodStall = (CardView) view.findViewById(R.id.edit_food_stall);
        mCardViewEditFoodStall.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_search:
                if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
                    mCardViewEditFoodStall.setVisibility(View.GONE);
                    new searchFoodStall(mEditTextStallName.getText().toString()).execute();
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

        String stallName;
        RequestHandler rh = new RequestHandler();

        public searchFoodStall(String stallName) {
            this.stallName = stallName;
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

            switch (foodStall.getResponse()){
                case 1:
                    // bus route found
                    mCardViewEditFoodStall.setVisibility(View.VISIBLE);
                    mEditTextStallName.setText("");
                    initialValues();
                    break;

                case 2:
                    // bus route inactive
                    mCardViewEditFoodStall.setVisibility(View.GONE);
                    new BaseActivity().shortToast(getActivity(), "No Record.");
                    break;

                case 0:
                    // bus route not found
                    mCardViewEditFoodStall.setVisibility(View.GONE);
                    new BaseActivity().shortToast(getActivity(), "No Record.");
                    break;

                default:
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("stallName", stallName);
            return rh.sendPostRequest(SEARCH_FOOD_STALL_URL, data);
        }
    }

    private void extractJsonData(String json) {
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            foodStall.setFoodStallID(jsonObject.getString(FoodStallRecord.COLUMN_FOOD_STALL_ID));
            foodStall.setAccountID(jsonObject.getString(FoodStallRecord.COLUMN_ACCOUNT_ID));
            foodStall.setStallName(jsonObject.getString(FoodStallRecord.COLUMN_STALL_NAME));
            foodStall.setJoinedDate(jsonObject.getString(FoodStallRecord.COLUMN_JOINED_DATE));
            foodStall.setStatus(jsonObject.getString(FoodStallRecord.COLUMN_STATUS));
            foodStall.setResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialValues() {
        mTextViewStallName.setText(foodStall.getStallName());
        mTextViewOwnerID.setText(foodStall.getAccountID());
        mTextViewJoinedDate.setText(foodStall.getJoinedDate());
        mTextViewStatus.setText(foodStall.getStatus());
    }
}
