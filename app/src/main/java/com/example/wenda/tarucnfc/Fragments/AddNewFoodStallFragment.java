package com.example.wenda.tarucnfc.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.FoodStall;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddNewFoodStallFragment extends Fragment implements View.OnClickListener {

    private EditText mEditTextAccountID;
    private EditText mEditTextFoodStallName;
    private Button mButtonConfirm;

    private FoodStall foodStall = new FoodStall();
    public Calendar calendar;
    public static final String KEY_RESPONSE = "Response";
    public SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private final static String Add_FOOD_STALL_URL = "http://fypproject.host56.com/FoodOrder/add_food_stall.php";

    public AddNewFoodStallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_food_stall, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        // set current date time
        calendar = Calendar.getInstance();

        return view;
    }

    private void setFindviewbyid(View view) {
        mEditTextAccountID = (EditText) view.findViewById(R.id.editText_accountID);
        mEditTextFoodStallName = (EditText) view.findViewById(R.id.editText_foodstall_name);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                // verify input data
                verifyData();
                break;

            default:
                break;
        }
    }

    public void verifyData(){
        try {
            foodStall.verifyAccountID(mEditTextAccountID.getText().toString());
            foodStall.verifyStallName(mEditTextFoodStallName.getText().toString());

            // insert data
            addData();
        } catch (InvalidInputException e) {
            new BaseActivity().shortToast(getActivity(), e.getInfo());
        }
    }

    public void addData(){
        // set all the related values into account domain
        foodStall.setAccountID(mEditTextAccountID.getText().toString());
        foodStall.setStallName(mEditTextFoodStallName.getText().toString());

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            new AddFoodStall(foodStall).execute();
            mEditTextAccountID.setSelection(0);
            mEditTextFoodStallName.setSelection(0);
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    public class AddFoodStall extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        FoodStall foodStall;

        public AddFoodStall(FoodStall foodStall) {
            this.foodStall = foodStall;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(getActivity(), "ON");
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //UIUtils.getProgressDialog(getActivity(), "OFF");
            //Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            extractJsonData(json);
            switch (foodStall.getResponse()){
                case 1:
                    //correct account
                    new BaseActivity().shortToast(getActivity(), "New Food Stall Created.");
                    mEditTextAccountID.setText("");
                    mEditTextFoodStallName.setText("");
                    break;

                case 2:
                    // account no found
                    new BaseActivity().shortToast(getActivity(), "Account ID no found.");
                    break;

                case 3:
                    // account type incorrect
                    new BaseActivity().shortToast(getActivity(), "Incorrect Account Type.");
                    break;

                case 4:
                    // account authorization incorrect
                    new BaseActivity().shortToast(getActivity(), "Incorrect Account Authorization");
                    break;
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", this.foodStall.getAccountID());
            data.put("stallName", this.foodStall.getStallName());
            data.put("joinedDate", dateTimeFormat.format(calendar.getTime()));

            return requestHandler.sendPostRequest(Add_FOOD_STALL_URL, data);
        }
    }

    private void extractJsonData(String json) {
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            foodStall.setResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
