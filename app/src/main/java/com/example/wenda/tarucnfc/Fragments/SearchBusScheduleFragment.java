package com.example.wenda.tarucnfc.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Activitys.EditBusRouteActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchBusScheduleFragment extends Fragment implements View.OnClickListener{

    private Spinner mSpinnerDestination;
    private Spinner mSpinnerDate;
    private Button mButtonSearch;
    private CardView mCardViewEditBusSchedule;
    private TextView mTextViewRouteDate;
    private TextView mTextViewDeparture;
    private TextView mTextViewDestination;
    private TextView mTextViewRouteTime;
    private String busRouteID;

    private static final String SEARCH_BUS_ROUTE_URL = "http://fypproject.host56.com/BusSchedule/search_bus_route.php";
    private static final String KEY_RESPONSE = "Response";
    private BusSchedule busSchedule = new BusSchedule();

    public SearchBusScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_bus_schedule, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        mCardViewEditBusSchedule.setVisibility(View.GONE);

        return view;
    }

    public void setFindviewbyid(View view){
        mCardViewEditBusSchedule = (CardView) view.findViewById(R.id.edit_bus_schedule);
        mCardViewEditBusSchedule.setOnClickListener(this);
        mSpinnerDestination = (Spinner) view.findViewById(R.id.spinner_bus_destination);
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_bus_date);
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mTextViewRouteDate = (TextView) view.findViewById(R.id.text_route_date);
        mTextViewDeparture = (TextView) view.findViewById(R.id.text_departure);
        mTextViewDestination = (TextView) view.findViewById(R.id.text_destination);
        mTextViewRouteTime = (TextView) view.findViewById(R.id.text_route_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
                    mCardViewEditBusSchedule.setVisibility(View.GONE);
                    new searchBusRoute(mSpinnerDestination.getSelectedItem().toString(), mSpinnerDate.getSelectedItem().toString()).execute();
                } else {
                    BaseActivity.shortToast(getActivity(), "Network not available");
                }
                break;

            case R.id.edit_bus_schedule:
                if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
                    mCardViewEditBusSchedule.setVisibility(View.GONE);
                    mSpinnerDestination.setSelection(0);
                    mSpinnerDate.setSelection(0);
                    Intent intent = new Intent(getActivity(), EditBusRouteActivity.class);
                    intent.putExtra("BusRouteID", busRouteID);
                    startActivity(intent);
                } else {
                    BaseActivity.shortToast(getActivity(), "Network not available");
                }
                break;

            default:
                break;
        }
    }

    // this is get json
    public class searchBusRoute extends AsyncTask<String, Void, String> {

        String destination, date;
        RequestHandler rh = new RequestHandler();

        public searchBusRoute(String destination, String date) {
            this.destination = destination;
            this.date = date;
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

            switch (busSchedule.getResponse()){
                case 1:
                    // bus route found
                    mCardViewEditBusSchedule.setVisibility(View.VISIBLE);
                    initialValues();
                    break;

                case 2:
                    // bus route inactive
                    mCardViewEditBusSchedule.setVisibility(View.GONE);
                    new BaseActivity().shortToast(getActivity(), "No Record.");
                    break;

                case 0:
                    // bus route not found
                    mCardViewEditBusSchedule.setVisibility(View.GONE);
                    new BaseActivity().shortToast(getActivity(), "No Record.");
                    break;

                default:
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("destination", destination);
            data.put("routeDay", date);
            return rh.sendPostRequest(SEARCH_BUS_ROUTE_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            busSchedule.setBusScheduleID(jsonObject.getString(BusScheduleRecord.COLUMN_BUS_SCHEDULE_ID));
            busSchedule.setBackEndID(jsonObject.getString(BusScheduleRecord.COLUMN_BACKEND_ID));
            busSchedule.setDeparture(jsonObject.getString(BusScheduleRecord.COLUMN_DEPARTURE));
            busSchedule.setDestination(jsonObject.getString(BusScheduleRecord.COLUMN_DESTINATION));
            busSchedule.setRouteDay(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_DAY));
            busSchedule.setRouteTime(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_TIME));
            busSchedule.setStatus(jsonObject.getString(BusScheduleRecord.COLUMN_STATUS));
            busSchedule.setResponse(jsonObject.getInt(KEY_RESPONSE));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialValues() {
        mTextViewRouteDate.setText(busSchedule.getRouteDay());
        mTextViewDeparture.setText(busSchedule.getDeparture());
        mTextViewDestination.setText(busSchedule.getDestination());
        mTextViewRouteTime.setText(busSchedule.getRouteTime());
        busRouteID = busSchedule.getBusScheduleID();
    }
}
