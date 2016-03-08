package com.example.wenda.tarucnfc.Fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MaintainReportFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;
    private JSONArray mJsonArray;
    private TextView mTextViewMessage;
    private SwipeRefreshLayout mSwipeContainer;
    private final static String GET_JSON_URL = "http://fypproject.host56.com/FoodOrder/get_food_report.php";
    private String foodItem1 = "", foodItem2 = "", foodItem3 = "", foodItem4 = "", foodItem5 = "";
    private int quantity1 = 0, quantity2 = 0, quantity3 = 0, quantity4 = 0, quantity5 = 0;

    public MaintainReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_maintain_report, container, false);

        //barChart = (BarChart) view.findViewById(R.id.barchart);
        mTextViewMessage = (TextView) view.findViewById(R.id.message);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        /*barChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });*/

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            new GetJson(GET_JSON_URL).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    new GetJson(GET_JSON_URL).execute();
                } else {
                    new BaseActivity().shortToast(getActivity(), "Network not available, couldn't refresh.");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pieChart.startAnimation();
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String url;
        RequestHandler rh = new RequestHandler();

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
                FoodOrder foodOrder = new FoodOrder();

                foodOrder.setFoodName(jsonObject.getString("FoodName"));
                foodOrder.setItemQuantity(jsonObject.getString("ItemQuantity"));

                switch (i){
                    case 0:
                        foodItem1 = foodOrder.getFoodName();
                        quantity1 = Integer.parseInt(foodOrder.getItemQuantity());
                        break;
                    case 1:
                        foodItem2 = foodOrder.getFoodName();
                        quantity2 = Integer.parseInt(foodOrder.getItemQuantity());
                        break;
                    case 2:
                        foodItem3 = foodOrder.getFoodName();
                        quantity3 = Integer.parseInt(foodOrder.getItemQuantity());
                        break;
                    case 3:
                        foodItem4 = foodOrder.getFoodName();
                        quantity4 = Integer.parseInt(foodOrder.getItemQuantity());
                        break;
                    case 4:
                        foodItem5 = foodOrder.getFoodName();
                        quantity5 = Integer.parseInt(foodOrder.getItemQuantity());
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        if (mJsonArray.length() > 0) {
            mTextViewMessage.setVisibility(View.GONE);
            startBarChart();
        } else {
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    private void startBarChart() {
        pieChart.clearChart();
        pieChart.addPieSlice(new PieModel(foodItem1, quantity1, Color.parseColor("#FE6DA8")));
        pieChart.addPieSlice(new PieModel(foodItem2, quantity2, Color.parseColor("#56B7F1")));
        pieChart.addPieSlice(new PieModel(foodItem3, quantity3, Color.parseColor("#CDA67F")));
        pieChart.addPieSlice(new PieModel(foodItem4, quantity4, Color.parseColor("#FED70E")));
        pieChart.addPieSlice(new PieModel(foodItem5, quantity5, 0xFF009688));
        pieChart.startAnimation();
    }

}
