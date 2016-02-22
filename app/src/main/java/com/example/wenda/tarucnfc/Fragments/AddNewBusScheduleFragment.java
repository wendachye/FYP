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
import android.widget.Spinner;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import java.util.HashMap;


public class AddNewBusScheduleFragment extends Fragment implements View.OnClickListener{

    private Spinner mSpinnerDeparture;
    private Spinner mSpinnerDestination;
    private Spinner mSpinnerDate;
    private EditText mEditTextTime;
    private Button mButtonConfirm;

    private BusSchedule busSchedule = new BusSchedule();
    private final static String Add_NEW_BUS_ROUTE_URL = "http://fypproject.host56.com/BusSchedule/add_bus_route.php";

    public AddNewBusScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_bus_schedule, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        return view;
    }

    public void setFindviewbyid(View view){
        mSpinnerDeparture = (Spinner) view.findViewById(R.id.spinner_bus_departure);
        mSpinnerDestination = (Spinner) view.findViewById(R.id.spinner_bus_destination);
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_bus_date);
        mEditTextTime = (EditText) view.findViewById(R.id.editText_bus_time);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                // verify input data
                verifyData();
                // insert data
                addData();
                break;

            default:
                break;
        }
    }

    public void verifyData(){

    }

    public void addData(){
        // set all the related values into account domain
        busSchedule.setDeparture(mSpinnerDeparture.getSelectedItem().toString());
        busSchedule.setDestination(mSpinnerDestination.getSelectedItem().toString());
        busSchedule.setRouteDay(mSpinnerDate.getSelectedItem().toString());
        busSchedule.setRouteTime(mEditTextTime.getText().toString());
        busSchedule.setBackEndID(String.valueOf(new BaseActivity().getLoginDetail(getActivity()).getLoginId()));

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            new AddBusRoute(busSchedule).execute();
            mSpinnerDeparture.setSelection(0);
            mSpinnerDestination.setSelection(0);
            mSpinnerDate.setSelection(0);
            mEditTextTime.setText("");
            BaseActivity.shortToast(getActivity(), "New Bus Route Created.");
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    public class AddBusRoute extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        BusSchedule busSchedule;

        public AddBusRoute(BusSchedule busSchedule) {
            this.busSchedule = busSchedule;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(getActivity(), "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //UIUtils.getProgressDialog(getActivity(), "OFF");
            //Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("backendID", this.busSchedule.getBackEndID());
            data.put("departure", this.busSchedule.getDeparture());
            data.put("destination", this.busSchedule.getDestination());
            data.put("routeTime", this.busSchedule.getRouteTime());
            data.put("routeDay", this.busSchedule.getRouteDay());

            return requestHandler.sendPostRequest(Add_NEW_BUS_ROUTE_URL, data);
        }
    }

}
