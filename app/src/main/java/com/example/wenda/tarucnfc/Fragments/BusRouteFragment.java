package com.example.wenda.tarucnfc.Fragments;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.wenda.tarucnfc.Activitys.BaseActivity.shortToast;

public class BusRouteFragment extends Fragment implements View.OnClickListener {

    private String condition;
    Button mButtonBusRoute;
    TextView mTextViewDate, mTextViewDate2, mTextViewDate3;
    TextView mTextViewDeparture, mTextViewDeparture2, mTextViewDeparture3;
    TextView mTextViewDestination, mTextViewDestination2, mTextViewDestination3;
    TextView mTextViewTime, mTextViewTime2, mTextViewTime3;
    CardView mCardView1, mCardView2, mCardView3;
    SwipeRefreshLayout mSwipeContainer;

    private BusSchedule busSchedule = new BusSchedule();
    private static final String GET_BUS_SCHEDULES_URL = "http://tarucandroid.comxa.com/BusSchedule/get_bus_schedule_view.php";
    private JSONArray mJsonArray;

    public BusRouteFragment() {
        // Required empty public constructor
    }

    public BusRouteFragment(String condition) {
        this.condition = condition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_route, container, false);

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetJson(String.valueOf(condition)).execute();
            }
        });

        // set findviewbyid
        setFindviewbyid(view);

        new GetJson(String.valueOf(condition)).execute();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        UIUtils.getProgressDialog(getActivity(), "OFF");
    }

    public void setFindviewbyid(View view) {
        mButtonBusRoute = (Button) view.findViewById(R.id.button_bus_route);

        mTextViewDate = (TextView) view.findViewById(R.id.text_route_date);
        mTextViewDate2 = (TextView) view.findViewById(R.id.text_route_date2);
        mTextViewDate3 = (TextView) view.findViewById(R.id.text_route_date3);

        mTextViewDeparture = (TextView) view.findViewById(R.id.text_departure);
        mTextViewDeparture2 = (TextView) view.findViewById(R.id.text_departure2);
        mTextViewDeparture3 = (TextView) view.findViewById(R.id.text_departure3);

        mTextViewDestination = (TextView) view.findViewById(R.id.text_destination);
        mTextViewDestination2 = (TextView) view.findViewById(R.id.text_destination2);
        mTextViewDestination3 = (TextView) view.findViewById(R.id.text_destination3);

        mTextViewTime = (TextView) view.findViewById(R.id.text_route_time);
        mTextViewTime2 = (TextView) view.findViewById(R.id.text_route_time2);
        mTextViewTime3 = (TextView) view.findViewById(R.id.text_route_time3);

        mCardView1 = (CardView) view.findViewById(R.id.cardview1);
        mCardView1.setOnClickListener(this);
        mCardView2 = (CardView) view.findViewById(R.id.cardview2);
        mCardView2.setOnClickListener(this);
        mCardView3 = (CardView) view.findViewById(R.id.cardview3);
        mCardView3.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_bus_route:
                Dialog settingsDialog = new Dialog(getContext());
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.route_wangsamaju
                        , null));
                settingsDialog.show();
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String destination;
        RequestHandler rh = new RequestHandler();

        public GetJson(String destination) {
            this.destination = destination;
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
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("destination", String.valueOf(condition));
            return rh.sendPostRequest(GET_BUS_SCHEDULES_URL, data);
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

    private void extractJsonData(String json) {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);

                busSchedule.setDeparture(jsonObject.getString(BusScheduleRecord.COLUMN_DEPARTURE));
                busSchedule.setDestination(jsonObject.getString(BusScheduleRecord.COLUMN_DESTINATION));
                busSchedule.setRouteTime(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_TIME));
                busSchedule.setRouteDay(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_DAY));
                busSchedule.setStatus(jsonObject.getString(BusScheduleRecord.COLUMN_STATUS));

                if (busSchedule.getStatus().equals("Active")) {
                    switch (busSchedule.getRouteDay()) {
                        case "Monday to Thursday":
                            initialValuesMondaytoThursday();
                            break;
                        case "Friday":
                            initialValuesFriday();
                            break;
                        case "Saturday":
                            initialValuesSaturday();
                        default:
                            break;
                    }
                } else{
                    shortToast(getActivity(),"This bus route is not available now.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
    }

    public void initialValuesMondaytoThursday() {
        mTextViewDate.setText(busSchedule.getRouteDay());
        mTextViewDeparture.setText(busSchedule.getDeparture());
        mTextViewDestination.setText(busSchedule.getDestination());
        mTextViewTime.setText(busSchedule.getRouteTime());
    }

    public void initialValuesFriday() {
        mTextViewDate2.setText(busSchedule.getRouteDay());
        mTextViewDeparture2.setText(busSchedule.getDeparture());
        mTextViewDestination2.setText(busSchedule.getDestination());
        mTextViewTime2.setText(busSchedule.getRouteTime());
    }

    public void initialValuesSaturday() {
        mTextViewDate3.setText(busSchedule.getRouteDay());
        mTextViewDeparture3.setText(busSchedule.getDeparture());
        mTextViewDestination3.setText(busSchedule.getDestination());
        mTextViewTime3.setText(busSchedule.getRouteTime());
    }

}