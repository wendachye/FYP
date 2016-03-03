package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.AdapterFoodOrder;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodStallContract;
import com.example.wenda.tarucnfc.Domains.FoodStall;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class FoodOrderFragment extends Fragment implements AdapterFoodOrder.AdapterCallBack{

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    private AdapterFoodOrder adapterFoodOrder;
    private JSONArray mJsonArray;
    private ArrayList<FoodStall> mListFoodStall = new ArrayList<>();
    final static String GET_FOOD_STALL_URL = "http://fypproject.host56.com/FoodOrder/get_food_stall.php";

    public FoodOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_order, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            mListFoodStall.clear();
            new GetJson(GET_FOOD_STALL_URL).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    mListFoodStall.clear();
                    new GetJson(GET_FOOD_STALL_URL).execute();
                } else {
                    new BaseActivity().shortToast(getActivity(), "Network not available, couldn't refresh.");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void adapterOnClick(int adapterPosition) {

    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {

        String url;

        public GetJson(String url) {
            this.url = url;
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
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bufferedReader;

            try {
                URL url = new URL(this.url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }

                return sb.toString().trim();

            } catch (Exception e) {
                return null;
            }
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
                FoodStall foodStall = new FoodStall();

                foodStall.setFoodStallID(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_FOOD_STALL_ID));
                foodStall.setStallName(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_STALL_NAME));
                foodStall.setLocation(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_LOCATION));
                foodStall.setFoodStallImagePath(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_FOOD_STALL_IMAGE_PATH));
                foodStall.setStatus(jsonObject.getString(FoodStallContract.FoodStallRecord.COLUMN_STATUS));

                if (foodStall.getStatus().toString().equals("Active")) {
                    mListFoodStall.add(foodStall);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        adapterFoodOrder = new AdapterFoodOrder(getActivity(), mListFoodStall, R.layout.row_food_stall, this);
        mRecyclerView.setAdapter(adapterFoodOrder);
    }

}
