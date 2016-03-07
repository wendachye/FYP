package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


public class EnduserReportFragment extends Fragment {

    private Calendar calendar;
    private String thisMonth;
    private String transactionMonth;
    private SwipeRefreshLayout mSwipeContainer;
    private String accountID;
    private JSONArray mJsonArray;
    private int diffMonth;
    private String month1 = "", month2 = "", month3 = "", m1 = "1", m2 = "2", m3 = "3";
    private BarChart barChart;
    private TextView mTextViewMessage, mTextViewMonth1, mTextViewMonth2, mTextViewMonth3;
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    double totalPrice, totalPrice2, totalPrice3;
    private final static String GET_JSON_URL = "http://fypproject.host56.com/FoodOrder/get_payment_report.php";

    public EnduserReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enduser_report, container, false);

        // set date time
        calendar = Calendar.getInstance();
        thisMonth = monthFormat.format(calendar.getTime());
        barChart = (BarChart) view.findViewById(R.id.barchart);
        mTextViewMessage = (TextView) view.findViewById(R.id.message);
        mTextViewMonth1 = (TextView) view.findViewById(R.id.month1);
        mTextViewMonth2 = (TextView) view.findViewById(R.id.month2);
        mTextViewMonth3 = (TextView) view.findViewById(R.id.month3);

        barChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                Log.d("BarChart", "Position: " + _Position);
            }
        });

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            totalPrice = 0.00;
            totalPrice2 = 0.00;
            totalPrice3 = 0.00;
            accountID = new BaseActivity().getLoginDetail(getActivity()).getAccountID();
            new GetJson(String.valueOf(accountID)).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    totalPrice = 0.00;
                    totalPrice2 = 0.00;
                    totalPrice3 = 0.00;
                    accountID = new BaseActivity().getLoginDetail(getActivity()).getAccountID();
                    new GetJson(String.valueOf(accountID)).execute();
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
        barChart.startAnimation();
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
            return rh.sendPostRequest(GET_JSON_URL, data);
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

                foodOrder.setTotalPrice(jsonObject.getString("TotalPrice"));
                foodOrder.setPaymentDateTime(jsonObject.getString("PaymentDateTime"));

                transactionMonth = foodOrder.getPaymentDateTime().substring(3,5);
                diffMonth = Integer.parseInt(thisMonth) - Integer.parseInt(transactionMonth);

                switch (diffMonth){
                    case 0:
                        month1 = transactionMonth;
                        totalPrice += Double.parseDouble(foodOrder.getTotalPrice());
                        break;

                    case 1:
                        month2 = transactionMonth;
                        totalPrice2 += Double.parseDouble(foodOrder.getTotalPrice());
                        break;

                    case 2:
                        month3 = transactionMonth;
                        totalPrice3 += Double.parseDouble(foodOrder.getTotalPrice());
                        break;

                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        if (mJsonArray.length() > 0) {
            setMonth(month1, m1);
            setMonth(month2, m2);
            setMonth(month3, m3);
            barChart.setVisibility(View.VISIBLE);
            mTextViewMonth1.setVisibility(View.VISIBLE);
            mTextViewMonth2.setVisibility(View.VISIBLE);
            mTextViewMonth3.setVisibility(View.VISIBLE);
            mTextViewMessage.setVisibility(View.GONE);
            startBarChart();
        } else {
            mTextViewMessage.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
        }
    }

    private void setMonth(String month, String m){
        String January = "January", February = "February", March = "March",
                April = "April", May = "May", June = "June", July = "July",
                August = "August", September = "September", October = "October",
                November = "November", December = "December";

        switch (month){
            case "01":
                month = January;
                break;
            case "02":
                month = February;
                break;
            case "03":
                month = March;
                break;
            case "04":
                month = April;
                break;
            case "05":
                month = May;
                break;
            case "06":
                month = June;
                break;
            case "07":
                month = July;
                break;
            case "08":
                month = August;
                break;
            case "09":
                month = September;
                break;
            case "10":
                month = October;
                break;
            case "11":
                month = November;
                break;
            case "12":
                month = December;
                break;
        }

        if (m.equals("1")){
            mTextViewMonth1.setText(month);
        } else if (m.equals("2")){
            mTextViewMonth2.setText(month);
        } else if (m.equals("3")){
            mTextViewMonth3.setText(month);
        }
    }

    private void startBarChart() {
        barChart.addBar(new BarModel((float)totalPrice, 0xFF009688));
        barChart.addBar(new BarModel((float)totalPrice2, 0xFF009688));
        barChart.addBar(new BarModel((float)totalPrice3, 0xFF009688));
        barChart.startAnimation();
    }

}
