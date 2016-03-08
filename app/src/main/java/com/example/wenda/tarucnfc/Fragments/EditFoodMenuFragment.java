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
import android.widget.LinearLayout;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Adapter.AdapterFoodItem;
import com.example.wenda.tarucnfc.Databases.Contracts.FoodMenuContract;
import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditFoodMenuFragment extends Fragment implements AdapterFoodItem.AdapterCallBack{

    private AdapterFoodItem adapterFoodItem;
    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    private JSONArray mJsonArray;
    private LinearLayout mLinearLayoutNoRecord;
    private ArrayList<FoodMenu> mListFoodMenu = new ArrayList<>();

    final static String GET_FOOD_MENU_URL = "http://fypproject.host56.com/FoodOrder/get_food_menu3.php";

    public EditFoodMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_food_menu, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLinearLayoutNoRecord = (LinearLayout) view.findViewById(R.id.layout_no_record);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            mListFoodMenu.clear();
            new GetJson(new BaseActivity().getLoginDetail(getActivity()).getAccountID()).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    mListFoodMenu.clear();
                    new GetJson(new BaseActivity().getLoginDetail(getActivity()).getAccountID()).execute();
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

        String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String accountID) {
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
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", accountID);
            return rh.sendPostRequest(GET_FOOD_MENU_URL, data);
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
                FoodMenu foodMenu = new FoodMenu();

                foodMenu.setFoodMenuID(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_MENU_ID));
                foodMenu.setFoodStallID(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_STALL_ID));
                foodMenu.setFoodName(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_NAME));
                foodMenu.setFoodCategory(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_CATEGORY));
                foodMenu.setFoodPrice(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_PRICE));
                foodMenu.setFoodMenuImagePath(jsonObject.getString(FoodMenuContract.FoodMenuRecord.COLUMN_FOOD_MENU_IMAGE_PATH));

                mListFoodMenu.add(foodMenu);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }

        if(mJsonArray.length() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapterFoodItem = new AdapterFoodItem(getActivity(), mListFoodMenu, R.layout.row_food_menu, this);
            mRecyclerView.setAdapter(adapterFoodItem);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }

}
