package com.example.wenda.tarucnfc.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import java.util.ArrayList;
import java.util.HashMap;


public class AddNewBusScheduleFragment extends Fragment implements View.OnClickListener{

    private Spinner mSpinnerDeparture;
    private Spinner mSpinnerDestination;
    private Spinner mSpinnerDate;
    private EditText mEditTextTime;
    private Button mButtonConfirm, mButtonClear;
    private CheckBox mMonday, mTuesday, mWednesday, mThursday, mFriday, mSaturday, mSunday;
    private ArrayList <String> Day = new ArrayList<>();

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
        //mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_bus_date);
        mMonday = (CheckBox) view.findViewById(R.id.chkMonday);
        mMonday.setOnClickListener(this);
        mTuesday = (CheckBox) view.findViewById(R.id.chkTuesday);
        mTuesday.setOnClickListener(this);
        mWednesday = (CheckBox) view.findViewById(R.id.chkWednesday);
        mWednesday.setOnClickListener(this);
        mThursday = (CheckBox) view.findViewById(R.id.chkThursday);
        mThursday.setOnClickListener(this);
        mFriday = (CheckBox) view.findViewById(R.id.chkFriday);
        mFriday.setOnClickListener(this);
        mSaturday = (CheckBox) view.findViewById(R.id.chkSaturday);
        mSaturday.setOnClickListener(this);
        mSunday = (CheckBox) view.findViewById(R.id.chkSunday);
        mSunday.setOnClickListener(this);
        mEditTextTime = (EditText) view.findViewById(R.id.editText_bus_time);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
        mButtonClear = (Button) view.findViewById(R.id.button_clear);
        mButtonClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                // verify input data
                verifyData();
                break;
            case R.id.button_clear:
                Day.clear();
                if (mMonday.isChecked()) {
                    mMonday.toggle();
                }
                if (mTuesday.isChecked()) {
                    mTuesday.toggle();
                }
                if(mWednesday.isChecked()) {
                    mWednesday.toggle();
                }
                if(mThursday.isChecked()) {
                    mThursday.toggle();
                }
                if(mFriday.isChecked()) {
                    mFriday.toggle();
                }
                if(mSaturday.isChecked()) {
                    mSaturday.toggle();
                }
                if(mSunday.isChecked()) {
                    mSunday.toggle();
                }
                break;
            case R.id.chkMonday:
                if (mMonday.isChecked()) {
                    Day.add("Monday");
                }
                break;
            case R.id.chkTuesday:
                if (mTuesday.isChecked()) {
                    Day.add("Tuesday");
                }
                break;
            case R.id.chkWednesday:
                if (mWednesday.isChecked()) {
                    Day.add("Wednesday");
                }
                break;
            case R.id.chkThursday:
                if (mThursday.isChecked()) {
                    Day.add("Thursday");
                }
                break;
            case R.id.chkFriday:
                if (mFriday.isChecked()) {
                    Day.add("Friday");
                }
                break;
            case R.id.chkSaturday:
                if (mSaturday.isChecked()) {
                    Day.add("Saturday");
                }
                break;
            case R.id.chkSunday:
                if (mSunday.isChecked()) {
                    Day.add("Sunday");
                }
                break;

            default:
                break;
        }
    }

    public void verifyData(){
        try {
            busSchedule.verifyRouteTime(mEditTextTime.getText().toString());

            // insert data
            addData();

        } catch (InvalidInputException e) {
            new BaseActivity().shortToast(getActivity(), e.getInfo());
        }
    }

    public void addData(){
        // set all the related values into account domain
        busSchedule.setDeparture(mSpinnerDeparture.getSelectedItem().toString());
        busSchedule.setDestination(mSpinnerDestination.getSelectedItem().toString());
        busSchedule.setRouteTime(mEditTextTime.getText().toString());
        busSchedule.setBackEndID(String.valueOf(new BaseActivity().getLoginDetail(getActivity()).getLoginId()));

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            for (int i = 0; i < Day.size(); i++) {
                //busSchedule.setRouteDay(Day.get(i));
                Log.d("track", " " + Day.get(i));
                new AddBusRoute(busSchedule, Day.get(i)).execute();
            }
            clearField();
            BaseActivity.shortToast(getActivity(), "New Bus Route Created.");
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    private void clearField() {
        //Day.removeAll(Collection<>)
        Day.clear();
        mSpinnerDeparture.setSelection(0);
        mSpinnerDestination.setSelection(0);
        //mSpinnerDate.setSelection(0);
        mEditTextTime.setText("");
        if (mMonday.isChecked()) {
            mMonday.toggle();
        }
        if (mTuesday.isChecked()) {
            mTuesday.toggle();
        }
        if(mWednesday.isChecked()) {
            mWednesday.toggle();
        }
        if(mThursday.isChecked()) {
            mThursday.toggle();
        }
        if(mFriday.isChecked()) {
            mFriday.toggle();
        }
        if(mSaturday.isChecked()) {
            mSaturday.toggle();
        }
        if(mSunday.isChecked()) {
            mSunday.toggle();
        }
    }

    public class AddBusRoute extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        BusSchedule busSchedule;
        String routeDay;

        public AddBusRoute(BusSchedule busSchedule, String routeDay) {
            this.busSchedule = busSchedule;
            this.routeDay = routeDay;
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
            data.put("routeDay", routeDay);

            return requestHandler.sendPostRequest(Add_NEW_BUS_ROUTE_URL, data);
        }
    }

}
