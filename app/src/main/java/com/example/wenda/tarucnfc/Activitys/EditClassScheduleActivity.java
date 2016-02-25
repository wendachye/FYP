package com.example.wenda.tarucnfc.Activitys;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Databases.Contracts.ClassScheduleContract.ClassScheduleRecord;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class EditClassScheduleActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTextViewBackendID;
    private TextView mTextViewStatus;
    private TextView mTextViewFaculty;
    private TextView mTextViewProgramme;
    private TextView mTextViewGroupNo;
    private TextView mTextViewSubject;
    private EditText mEditTextTutorLecturer;
    private EditText mEditTextLocation;
    private Spinner mSpinnerDate;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private Button mButtonDelete;

    private String classScheduleID;
    private ClassSchedule classSchedule = new ClassSchedule();
    private final static String GET_JSON_URL = "http://fypproject.host56.com/ClassSchedule/edit_class_schedule_view.php";
    private final static String UPDATE_CLASS_SCHEDULE_URL = "http://fypproject.host56.com/ClassSchedule/update_class_schedule.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_schedule);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_edit_class_schedule);

        // set back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setfindviewbyid
        setFindviewbyid();

        calendar = Calendar.getInstance();

        classScheduleID = getIntent().getStringExtra("ClassScheduleID");

        new GetJson(classScheduleID).execute();
    }

    private void setFindviewbyid() {
        mTextViewBackendID = (TextView) findViewById(R.id.text_view_backendID);
        mTextViewStatus = (TextView) findViewById(R.id.text_view_status);
        mTextViewFaculty = (TextView) findViewById(R.id.text_view_faculty);
        mTextViewProgramme = (TextView) findViewById(R.id.text_view_programme);
        mTextViewGroupNo = (TextView) findViewById(R.id.text_view_groupNo);
        mTextViewSubject = (TextView) findViewById(R.id.text_view_subject);
        mEditTextTutorLecturer = (EditText) findViewById(R.id.editText_tutorlecturer);
        mEditTextLocation = (EditText) findViewById(R.id.editText_location);
        mSpinnerDate = (Spinner) findViewById(R.id.spinner_date);
        mTextViewStartTime = (TextView) findViewById(R.id.textView_startTime);
        mTextViewEndTime = (TextView) findViewById(R.id.textView_endTime);
        mButtonDelete = (Button) findViewById(R.id.button_delete);
        mButtonDelete.setOnClickListener(this);
        mTextViewStartTime.setOnClickListener(this);
        mTextViewEndTime.setOnClickListener(this);
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
            updateClassSchedule();
            finish();
            shortToast(this, "Class Schedule Updated.");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_startTime:
                TimePickerDialog.OnTimeSetListener timeStartListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        mTextViewStartTime.setText(BaseActivity.timeFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(this, timeStartListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                break;

            case R.id.textView_endTime:
                TimePickerDialog.OnTimeSetListener timeEndListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        mTextViewEndTime.setText(BaseActivity.timeFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(this, timeEndListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String classScheduleID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String classScheduleID) {
            this.classScheduleID = classScheduleID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditClassScheduleActivity.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(EditClassScheduleActivity.this, "OFF");
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("classScheduleID", classScheduleID);
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            classSchedule.setClassScheduleID(jsonObject.getString(ClassScheduleRecord.COLUMN_CLASS_SCHEDULE_ID));
            classSchedule.setBackendID(jsonObject.getString(ClassScheduleRecord.COLUMN_BACKEND_ID));
            classSchedule.setFaculty(jsonObject.getString(ClassScheduleRecord.COLUMN_FACULTY));
            classSchedule.setProgramme(jsonObject.getString(ClassScheduleRecord.COLUMN_PROGRAMME));
            classSchedule.setGroupNo(jsonObject.getString(ClassScheduleRecord.COLUMN_GROUP_No));
            classSchedule.setSubject(jsonObject.getString(ClassScheduleRecord.COLUMN_SUBJECT));
            classSchedule.setTutorlecturer(jsonObject.getString(ClassScheduleRecord.COLUMN_TUTORLECTURER));
            classSchedule.setLocation(jsonObject.getString(ClassScheduleRecord.COLUMN_LOCATION));
            classSchedule.setDay(jsonObject.getString(ClassScheduleRecord.COLUMN_DAY));
            classSchedule.setStartTime(jsonObject.getString(ClassScheduleRecord.COLUMN_START_TIME));
            classSchedule.setEndTime(jsonObject.getString(ClassScheduleRecord.COLUMN_END_TIME));
            classSchedule.setStatus(jsonObject.getString(ClassScheduleRecord.COLUMN_STATUS));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    private void initialValues() {
        switch (classSchedule.getDay()){
            case "Monday":
                mSpinnerDate.setSelection(0);
                break;

            case "Tuesday":
                mSpinnerDate.setSelection(1);
                break;

            case "Wednesday":
                mSpinnerDate.setSelection(2);
                break;
            case "Thursday":
                mSpinnerDate.setSelection(3);
                break;
            case "Friday":
                mSpinnerDate.setSelection(4);
                break;
            case "Saturday":
                mSpinnerDate.setSelection(5);
                break;
        }
        mTextViewBackendID.setText(classSchedule.getBackendID());
        mTextViewStatus.setText(classSchedule.getStatus());
        mTextViewFaculty.setText(classSchedule.getFaculty());
        mTextViewProgramme.setText(classSchedule.getProgramme());
        mTextViewGroupNo.setText(classSchedule.getGroupNo());
        mTextViewSubject.setText(classSchedule.getSubject());
        mEditTextTutorLecturer.setText(classSchedule.getTutorlecturer());
        mEditTextLocation.setText(classSchedule.getLocation());
        mTextViewStartTime.setText(classSchedule.getStartTime());
        mTextViewEndTime.setText(classSchedule.getEndTime());
    }

    //update class schedule
    private void updateClassSchedule() {
        // set all the related values into account domain
        classSchedule.setClassScheduleID(classScheduleID);
        classSchedule.setBackendID(String.valueOf(getLoginDetail(this).getLoginId()));
        classSchedule.setFaculty(mTextViewFaculty.getText().toString());
        classSchedule.setProgramme(mTextViewProgramme.getText().toString());
        classSchedule.setGroupNo(mTextViewGroupNo.getText().toString());
        classSchedule.setTutorlecturer(mEditTextTutorLecturer.getText().toString());
        classSchedule.setSubject(mTextViewSubject.getText().toString());
        classSchedule.setLocation(mEditTextLocation.getText().toString());
        classSchedule.setDay(mSpinnerDate.getSelectedItem().toString());
        classSchedule.setStartTime(mTextViewStartTime.getText().toString());
        classSchedule.setEndTime(mTextViewEndTime.getText().toString());
        classSchedule.setStatus(mTextViewStatus.getText().toString());

        // check network
        if(isNetworkAvailable(this) == true) {
            new UpdateClassSchedule(classSchedule).execute();
        } else {
            shortToast(this, "Network not available");
        }
    }

    public class UpdateClassSchedule extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        ClassSchedule classSchedule;

        public UpdateClassSchedule(ClassSchedule classSchedule) {
            this.classSchedule = classSchedule;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(EditClassScheduleActivity.this, "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UIUtils.getProgressDialog(EditClassScheduleActivity.this, "OFF");
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            finish();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("classScheduleID", this.classSchedule.getClassScheduleID());
            data.put("backendID", this.classSchedule.getBackendID());
            data.put("faculty", this.classSchedule.getFaculty());
            data.put("programme", this.classSchedule.getProgramme());
            data.put("groupNo", this.classSchedule.getGroupNo());
            data.put("subject", this.classSchedule.getSubject());
            data.put("tutorLecturer", this.classSchedule.getTutorlecturer());
            data.put("location", this.classSchedule.getLocation());
            data.put("date", this.classSchedule.getDay());
            data.put("startTime", this.classSchedule.getStartTime());
            data.put("endTime", this.classSchedule.getEndTime());
            data.put("status", this.classSchedule.getStatus());

            return requestHandler.sendPostRequest(UPDATE_CLASS_SCHEDULE_URL, data);
        }
    }
}
