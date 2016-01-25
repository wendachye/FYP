package com.example.wenda.tarucnfc.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.DataSources.BusScheduleDataSource;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BusRouteFragment extends Fragment implements View.OnClickListener {

    private String condition;

    //@Bind(R.id.swipeContainer)
    //SwipeRefreshLayout mSwipeContainer;
    //@Bind(R.id.spinner_date)
    Spinner mSpinnerDate;
    //@Bind(R.id.bus_route)
    Button mButtonBusRoute;
    //@Bind(R.id.route_title)
    TextView mTextViewRouteTitle;
    //@Bind(R.id.text_departure)
    TextView mTextViewDeparture;
    //@Bind(R.id.text_destination)
    TextView mTextViewDestination;
    //@Bind(R.id.text_time)
    TextView mTextViewTime;
    //@Bind(R.id.text_departure2)
    TextView mTextViewDeparture2;
    //@Bind(R.id.text_destination2)
    TextView mTextViewDestination2;
    //@Bind(R.id.text_time2)
    TextView mTextViewTime2;
    //@Bind(R.id.cardview1)
    CardView mCardView1;
    //@Bind(R.id.cardview2)
    CardView mCardView2;

    public SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final String GET_BUS_SCHEDULES_URL = "http://tarucandroid.comxa.com/BusSchedule/get_bus_schedule_data.php";
    public static final String KEY_BUS_SCHEDULE_ID = "busScheduleID";
    public static final String KEY_BACKEND_ID = "backendID";
    public static final String KEY_DEPARTURE = "departure";
    public static final String KEY_DESTINATION = "destination";
    public static final String KEY_ROUTE_TIME = "routeTime";
    public static final String KEY_ROUTE_DAY = "routeDay";
    public static final String KEY_STATUS = "status";
    private JSONArray mJsonArray;
    private ArrayList<BusSchedule> mListBusSchedules = new ArrayList<>();
    Calendar calendar;
    BusScheduleDataSource busScheduleDataSource;
    BusSchedule mModelBusSchedule;
    OfflineLogin offlineLogin;

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

        try {
            initialData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // set onClick
        mTextViewRouteTitle = (TextView) view.findViewById(R.id.route_title);
        mTextViewRouteTitle.setOnClickListener(this);
        mTextViewDeparture = (TextView) view.findViewById(R.id.text_departure);
        mTextViewDeparture.setOnClickListener(this);
        mTextViewDestination = (TextView) view.findViewById(R.id.text_destination);
        mTextViewDestination.setOnClickListener(this);
        mTextViewDeparture2 = (TextView) view.findViewById(R.id.text_departure2);
        mTextViewDeparture2.setOnClickListener(this);
        mTextViewDestination2 = (TextView) view.findViewById(R.id.text_destination2);
        mTextViewDestination2.setOnClickListener(this);
        mTextViewTime = (TextView) view.findViewById(R.id.text_time);
        mTextViewTime.setOnClickListener(this);
        mTextViewTime2 = (TextView) view.findViewById(R.id.text_time2);
        mTextViewTime2.setOnClickListener(this);
        mCardView1 = (CardView) view.findViewById(R.id.cardview1);
        mCardView1.setOnClickListener(this);
        mCardView2 = (CardView) view.findViewById(R.id.cardview2);
        mCardView2.setOnClickListener(this);
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_date);

        // set spinner text size and color
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.busSchedule_arrays, R.layout.spinner_bus);
        dateAdapter.setDropDownViewResource(R.layout.spinner_bus);
        mSpinnerDate.setAdapter(dateAdapter);

        switch (condition) {

            case "Wangsa Maju":
                mTextViewRouteTitle.setText("Wangsa Maju Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case  "Genting Klang":
                mTextViewRouteTitle.setText("Genting Klang Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "PV10/12/13/15/16":
                mTextViewRouteTitle.setText("PV10/12/13/15/16 Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Melati Utama":
                mTextViewRouteTitle.setText("Melati Utama Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Sri Rampai":
                mTextViewRouteTitle.setText("Sri Rampai Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            default:
                break;
        }

        return view;
    }

    private void initialData() throws SQLException {
        calendar = Calendar.getInstance();

        busScheduleDataSource = new BusScheduleDataSource(getActivity());
        busScheduleDataSource.open();
        if (busScheduleDataSource.isEmptyBusSchedule())
            new GetBusSchedules().execute(GET_BUS_SCHEDULES_URL);
        else
            initBusTime();

        /*mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    new GetBusSchedules().execute(GET_BUS_SCHEDULES_URL);
                } else {
                    shortToast(getActivity(), "Network not available, failed to refresh");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });*/

        checkUser();
    }

    public void checkUser() {
        offlineLogin = new BaseActivity().getLoginDetail(getActivity());

        /*if (offlineLogin != null) {
            if (offlineLogin.getAccountType().equals("Back End")) {
                String[] authorization = offlineLogin.getAuthorization().split("\\s+");
                for (String permission : authorization) {
                    if (permission.equals("BusSchedule")) {
                        mFabAdd.setVisibility(View.VISIBLE);
                        mFabNotification.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                mFabNotification.setVisibility(View.VISIBLE);
                mFabAdd.setVisibility(View.INVISIBLE);
            }
        } else {
            mFabNotification.setVisibility(View.VISIBLE);
            mFabAdd.setVisibility(View.INVISIBLE);
        }


        // // TODO: 1/20/2016 test this
        final Handler h = new Handler();
        final int delay = 1000; //milliseconds

        h.postDelayed(new Runnable() {
            public void run() {
                initBusTime();
                h.postDelayed(this, delay);
            }
        }, delay);*/


    }

    // this one is get json
    public class GetBusSchedules extends AsyncTask<String, Void, String> {
        ProgressDialog loading;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(getActivity(), "Loading...", "Please Wait...", true, true);
            //UIUtils.getProgressDialog(getActivity(), "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //loading.dismiss();
            //UIUtils.getProgressDialog(getActivity(), "OFF");
            convertJson(json);
            try {
                extractJsonData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //mSwipeContainer.setRefreshing(false);

        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader bufferedReader;
            try {
                URL url = new URL(params[0]);
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

    private void extractJsonData() throws SQLException {
        busScheduleDataSource.open();
        busScheduleDataSource.deleteBusSchedules();

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {

                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                BusSchedule busSchedule = new BusSchedule();

                busSchedule.setBusScheduleId(Integer.parseInt(jsonObject.getString(KEY_BUS_SCHEDULE_ID)));
                busSchedule.setBackEndId(Integer.parseInt(jsonObject.getString(KEY_BACKEND_ID)));
                busSchedule.setDeparture(jsonObject.getString(KEY_DEPARTURE));
                busSchedule.setDestination(jsonObject.getString(KEY_DESTINATION));
                busSchedule.setRouteTime(jsonObject.getString(KEY_ROUTE_TIME));
                busSchedule.setRouteDay(jsonObject.getString(KEY_ROUTE_DAY));
                busSchedule.setStatus(jsonObject.getString(KEY_STATUS));

                mListBusSchedules.add(busSchedule);
                Log.d("BusScheduleFragment", "Before insider : Schedule id : " + busSchedule.getBusScheduleId() +
                        "\nBack end id : " + busSchedule.getBackEndId() +
                        "\nDeparture time : " + busSchedule.getRouteTime() +
                        "\nDestination : " + busSchedule.getDestination() +
                        "\nDay : " + busSchedule.getRouteDay());

                busScheduleDataSource.insertBusSchedules(busSchedule);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        initBusTime();
    }

    public void initBusTime() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day != 1) {

            busScheduleDataSource.open();
            mModelBusSchedule = busScheduleDataSource.getBusScheduleByDestination(mTextViewDestination.getText().toString(), day + "");

            if (mModelBusSchedule.getRouteTime() != null) {

                Log.d("BusScheduleFragment", mModelBusSchedule.getRouteTime() + "");
                String[] arrayBusTime = mModelBusSchedule.getRouteTime().split("\\s+");

                ArrayList<String> arrayUpcomingBus = new ArrayList<>();

                for (String busTime : arrayBusTime) {
                    Log.d("BusScheduleFragment", busTime);
                    try {

                        if (timeFormat.format(timeFormat.parse(busTime)).equals((timeFormat.format(calendar.getTime())))
                                || (timeFormat.parse(busTime).after(timeFormat.parse(timeFormat.format(calendar.getTime()))))) {
                            arrayUpcomingBus.add(busTime);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                mTextViewDeparture.setText(mModelBusSchedule.getDeparture());
                if (arrayUpcomingBus.size() != 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String value : arrayUpcomingBus) {
                        builder.append(value + "  ");
                    }
                    String text = builder.toString();

                    //mTextUpcomingBus.setText(arrayUpcomingBus.get(0));
                    //mTextFollowingBus.setText(text);
                } else {
                    //mTextFollowingBus.setText("");
                    //mTextUpcomingBus.setText("");
                }

            } else {
                Log.d("BusScheduleFragment", "Is null and here ");
                //mTextFollowingBus.setText("");
               // mTextUpcomingBus.setText("");
            }
        } else {
           // mTextFollowingBus.setText(getResources().getString(R.string.no_bus_on_sunday));
            //mTextUpcomingBus.setText("");

        }

        //mTextDay.setText(arrayDay[day]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bus_route:
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

    @Override
    public void onResume() {
        super.onResume();
        switch (condition) {

            case "Wangsa Maju":
                mTextViewRouteTitle.setText("Wangsa Maju Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case  "Genting Klang":
                mTextViewRouteTitle.setText("Genting Klang Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "PV10/12/13/15/16":
                mTextViewRouteTitle.setText("PV10/12/13/15/16 Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Melati Utama":
                mTextViewRouteTitle.setText("Melati Utama Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Sri Rampai":
                mTextViewRouteTitle.setText("Sri Rampai Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);

                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            default:
                break;
        }
    }
}
