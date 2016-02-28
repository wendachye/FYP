package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditBusRouteActivity extends BaseActivity implements View.OnClickListener {

    private Spinner mSpinnerRouteDate;
    private EditText mEditTextRouteTime;
    private Spinner mSpinnerDeparture;
    private TextView mTextViewDestination;
    private TextView mTextViewBackendID;
    private TextView mTextViewStatus;
    private Button mButttonDelete;

    private String busRouteID;
    private BusSchedule busSchedule = new BusSchedule();
    private final static String GET_JSON_URL = "http://fypproject.host56.com/BusSchedule/edit_bus_schedule_view.php";
    private final static String UPDATE_BUS_ROUTE_URL = "http://fypproject.host56.com/BusSchedule/update_bus_route.php";
    private final static String DELETE_BUS_ROUTE_URL = "http://fypproject.host56.com/BusSchedule/delete_bus_route.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bus_route);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_edit_bus_route);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setfindviewbyid
        setFindviewbyid();

        busRouteID = getIntent().getStringExtra("BusRouteID");

        new GetJson(busRouteID).execute();
    }

    private void setFindviewbyid() {
        mSpinnerDeparture = (Spinner) findViewById(R.id.spinner_bus_departure);
        mTextViewDestination = (TextView) findViewById(R.id.text_view_destination);
        mSpinnerRouteDate = (Spinner) findViewById(R.id.spinner_bus_date);
        mEditTextRouteTime = (EditText) findViewById(R.id.edit_text_route_time);
        mTextViewBackendID = (TextView) findViewById(R.id.text_view_backendID);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);
        mButttonDelete = (Button) findViewById(R.id.button_delete);
        mButttonDelete.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_button, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        } else if (id == R.id.saveButton) {
            updateBusRoute();
            finish();
            shortToast(this, "Bus Route Updated.");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_delete:
                new DeleteBusRoute(busRouteID).execute();
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String busRouteID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String busRouteID) {
            this.busRouteID = busRouteID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("busScheduleID", busRouteID);
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            busSchedule.setBackEndID(jsonObject.getString(BusScheduleRecord.COLUMN_BACKEND_ID));
            busSchedule.setDeparture(jsonObject.getString(BusScheduleRecord.COLUMN_DEPARTURE));
            busSchedule.setDestination(jsonObject.getString(BusScheduleRecord.COLUMN_DESTINATION));
            busSchedule.setRouteDay(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_DAY));
            busSchedule.setRouteTime(jsonObject.getString(BusScheduleRecord.COLUMN_ROUTE_TIME));
            busSchedule.setStatus(jsonObject.getString(BusScheduleRecord.COLUMN_STATUS));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {
        switch (busSchedule.getDeparture()){
            case "Bus Stop 1":
                mSpinnerDeparture.setSelection(0);
                break;

            case "Bus Stop 2 and 3":
                mSpinnerDeparture.setSelection(1);
                break;

            case "Bus Stop 4 and 5":
                mSpinnerDeparture.setSelection(2);
                break;

            case "Bus Stop 6":
                mSpinnerDeparture.setSelection(3);
                break;

            case "Library":
                mSpinnerDeparture.setSelection(4);
                break;

            default:
                break;
        }

        switch (busSchedule.getRouteDay()){
            case "Monday to Thursday":
                mSpinnerRouteDate.setSelection(0);
                break;

            case "Friday":
                mSpinnerRouteDate.setSelection(1);
                break;

            case "Saturday":
                mSpinnerRouteDate.setSelection(2);
                break;

            default:
                break;
        }

        mTextViewDestination.setText(busSchedule.getDestination());
        mEditTextRouteTime.setText(busSchedule.getRouteTime());
        mTextViewBackendID.setText(busSchedule.getBackEndID());
        mTextViewStatus.setText(busSchedule.getStatus());
    }

    //update bus route
    private void updateBusRoute() {
        // set all the related values into account domain
        busSchedule.setBusScheduleID(busRouteID);
        busSchedule.setBackEndID(String.valueOf(getLoginDetail(this).getLoginId()));
        busSchedule.setDeparture(mSpinnerDeparture.getSelectedItem().toString());
        busSchedule.setDestination(mTextViewDestination.getText().toString());
        busSchedule.setRouteDay(mSpinnerRouteDate.getSelectedItem().toString());
        busSchedule.setRouteTime(mEditTextRouteTime.getText().toString());
        busSchedule.setStatus(mTextViewStatus.getText().toString());

        // check network
        if(isNetworkAvailable(this) == true) {
            new UpdateBusRoute(busSchedule).execute();
        } else {
            shortToast(this, "Network not available");
        }
    }

    public class UpdateBusRoute extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        BusSchedule busSchedule;

        public UpdateBusRoute(BusSchedule busSchedule) {
            this.busSchedule = busSchedule;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "OFF");
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("busScheduleID", this.busSchedule.getBusScheduleID());
            data.put("backendID", this.busSchedule.getBackEndID());
            data.put("departure", this.busSchedule.getDeparture());
            data.put("destination", this.busSchedule.getDestination());
            data.put("routeDay", this.busSchedule.getRouteDay());
            data.put("routeTime", this.busSchedule.getRouteTime());
            data.put("status", this.busSchedule.getStatus());

            return requestHandler.sendPostRequest(UPDATE_BUS_ROUTE_URL, data);
        }
    }

    public class DeleteBusRoute extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        String busRouteID;

        public DeleteBusRoute(String busRouteID) {
            this.busRouteID = busRouteID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditBusRouteActivity.this, "OFF");
            shortToast(EditBusRouteActivity.this, "Delete Bus Route Successful.");
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("busScheduleID", busRouteID);

            return requestHandler.sendPostRequest(DELETE_BUS_ROUTE_URL, data);
        }
    }
}
