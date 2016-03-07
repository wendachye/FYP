package com.example.wenda.tarucnfc.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Adapter.AdapterBusRoute;
import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.wenda.tarucnfc.Activitys.BaseActivity.shortToast;

public class BusRouteFragment extends Fragment implements View.OnClickListener {

    private String condition;
    private LinearLayout mLinearLayoutNoRecord;
    private SwipeRefreshLayout mSwipeContainer;
    private TextView mTextViewDay;
    private RecyclerView mRecyclerView;
    private BusSchedule busSchedule = new BusSchedule();
    private static final String GET_BUS_SCHEDULES_URL = "http://fypproject.host56.com/BusSchedule/get_bus_schedule_view.php";
    private JSONArray mJsonArray;
    private Calendar calendar;
    private AdapterBusRoute adapterBusRoute;
    private ArrayList<BusSchedule> mListBusSchedule = new ArrayList<>();
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

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

        // set current date time
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        mLinearLayoutNoRecord = (LinearLayout) view.findViewById(R.id.layout_no_record);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTextViewDay = (TextView) view.findViewById(R.id.bus_day);
        mTextViewDay.setOnClickListener(this);

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            switch (day){
                case Calendar.MONDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("MONDAY");
                    new GetJson(String.valueOf(condition), "Monday").execute();
                    break;
                case Calendar.TUESDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("TUESDAY");
                    new GetJson(String.valueOf(condition), "Tuesday").execute();
                    break;
                case Calendar.WEDNESDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("WEDNESDAY");
                    new GetJson(String.valueOf(condition), "Wednesday").execute();
                    break;
                case Calendar.THURSDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("THURSDAY");
                    new GetJson(String.valueOf(condition), "Thursday").execute();
                    break;
                case Calendar.FRIDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("FRIDAY");
                    new GetJson(String.valueOf(condition), "Friday").execute();
                    break;
                case Calendar.SATURDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("SATURDAY");
                    new GetJson(String.valueOf(condition), "Saturday").execute();
                    break;
                case Calendar.SUNDAY:
                    mListBusSchedule.clear();
                    mTextViewDay.setText("SUNDAY");
                    new GetJson(String.valueOf(condition), "Sunday").execute();
                    break;
                default:
                    break;
            }
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    switch (mTextViewDay.getText().toString()){
                        case "MONDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Monday").execute();
                            break;
                        case "TUESDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Tuesday").execute();
                            break;
                        case "WEDNESDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Wednesday").execute();
                            break;
                        case "THURSDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Thursday").execute();
                            break;
                        case "FRIDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Friday").execute();
                            break;
                        case "SATURDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Saturday").execute();
                            break;
                        case "SUNDAY":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "Sunday").execute();
                            break;
                        case "View All":
                            mListBusSchedule.clear();
                            new GetJson(String.valueOf(condition), "View All").execute();
                            break;
                        default:
                            break;
                    }
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

        //UIUtils.getProgressDialog(getActivity(), "OFF");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bus_day:
                new MaterialDialog.Builder(getActivity())
                        .items(R.array.items4)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("MONDAY");
                                        new GetJson(String.valueOf(condition), "Monday").execute();
                                        break;
                                    case 1:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("TUESDAY");
                                        new GetJson(String.valueOf(condition), "Tuesday").execute();
                                        break;
                                    case 2:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("WEDNESDAY");
                                        new GetJson(String.valueOf(condition), "Wednesday").execute();
                                        break;
                                    case 3:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("THURSDAY");
                                        new GetJson(String.valueOf(condition), "Thursday").execute();
                                        break;
                                    case 4:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("FRIDAY");
                                        new GetJson(String.valueOf(condition), "Friday").execute();
                                        break;
                                    case 5:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("SATURDAY");
                                        new GetJson(String.valueOf(condition), "Saturday").execute();
                                        break;
                                    case 6:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("SUNDAY");
                                        new GetJson(String.valueOf(condition), "Sunday").execute();
                                        break;
                                    case 7:
                                        mListBusSchedule.clear();
                                        mTextViewDay.setText("View All");
                                        new GetJson(String.valueOf(condition), "View All").execute();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String destination, day;
        RequestHandler rh = new RequestHandler();

        public GetJson(String destination, String day) {
            this.destination = destination;
            this.day = day;
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
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData(day);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("destination", destination);
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

    private void extractJsonData(String day) {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                BusSchedule busSchedule = new BusSchedule();

                busSchedule.setDeparture(jsonObject.getString(BusScheduleRecord.COLUMN_DEPARTURE));
                busSchedule.setDestination(jsonObject.getString(BusScheduleRecord.COLUMN_DESTINATION));
                //busSchedule.setRouteTime(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_TIME));

                String[] arrayBusTime = jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_TIME).split("\\s+");
                ArrayList<String> arrayUpcomingBus = new ArrayList<>();

                for (String busTime : arrayBusTime) {
                    try {
                        if (timeFormat.format(timeFormat.parse(busTime)).equals((timeFormat.format(calendar.getTime())))
                                || (timeFormat.parse(busTime).after(timeFormat.parse(timeFormat.format(calendar.getTime()))))) {
                            arrayUpcomingBus.add(busTime);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (arrayUpcomingBus.size() != 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String value : arrayUpcomingBus) {
                        builder.append(value + "  ");
                    }
                    String text = builder.toString();

                    busSchedule.setRouteTime(arrayUpcomingBus.get(0));
                } else {
                    busSchedule.setRouteTime("");
                }

                busSchedule.setRouteDay(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_DAY));
                busSchedule.setStatus(jsonObject.getString(BusScheduleRecord.COLUMN_STATUS));

                if (busSchedule.getStatus().equals("Active") && busSchedule.getRouteDay().equals(day)) {
                    switch (day){
                        case "Monday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Tuesday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Wednesday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Thursday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Friday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Saturday":
                            mListBusSchedule.add(busSchedule);
                            break;
                        case "Sunday":
                            mListBusSchedule.add(busSchedule);
                            break;
                    }
                } else if (busSchedule.getStatus().equals("Active") && day.equals("View All")){
                    mListBusSchedule.add(busSchedule);
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        if (mListBusSchedule.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapterBusRoute = new AdapterBusRoute(getActivity(), mListBusSchedule, R.layout.row_bus_route);
            mRecyclerView.setAdapter(adapterBusRoute);
            Log.d("track", "list" + mListBusSchedule.size());
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }

}