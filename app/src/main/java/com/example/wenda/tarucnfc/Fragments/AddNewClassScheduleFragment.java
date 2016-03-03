package com.example.wenda.tarucnfc.Fragments;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import java.util.Calendar;
import java.util.HashMap;

public class AddNewClassScheduleFragment extends Fragment implements View.OnClickListener{

    private Spinner mSpinnerFaculty;
    private Spinner mSpinnerClassType;
    private Spinner mSpinnerDate;
    private EditText mEditTextProgramme;
    private EditText mEditTextGroupNo;
    private EditText mEditTextSubject;
    private EditText mEditTextTutorLecturer;
    private EditText mEditTextLocation;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private Button mButtonConfirm;

    private ClassSchedule classSchedule = new ClassSchedule();
    private OfflineLogin offlineLogin = new OfflineLogin();
    Calendar calendar;
    private final static String Add_NEW_CLASS_TIMETABLE_URL = "http://fypproject.host56.com/ClassSchedule/add_class_schedule.php";

    public AddNewClassScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_class_schedule, container, false);

        calendar = Calendar.getInstance();

        // setfindviewbyid
        setFindviewbyid(view);



        return view;
    }

    public void setFindviewbyid(View view){
        mSpinnerFaculty = (Spinner) view.findViewById(R.id.spinner_faculty);
        mSpinnerClassType = (Spinner) view.findViewById(R.id.spinner_classType);
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_date);
        mEditTextProgramme = (EditText) view.findViewById(R.id.editText_programme);
        mEditTextGroupNo = (EditText) view.findViewById(R.id.editText_groupNo);
        mEditTextSubject = (EditText) view.findViewById(R.id.editText_subject);
        mEditTextTutorLecturer = (EditText) view.findViewById(R.id.editText_tutorlecturer);
        mEditTextLocation = (EditText) view.findViewById(R.id.editText_location);
        mTextViewStartTime = (TextView) view.findViewById(R.id.textView_startTime);
        mTextViewEndTime = (TextView) view.findViewById(R.id.textView_endTime);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mTextViewStartTime.setOnClickListener(this);
        mTextViewEndTime.setOnClickListener(this);
        mButtonConfirm.setOnClickListener(this);
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
                new TimePickerDialog(getActivity(), timeStartListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
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
                new TimePickerDialog(getActivity(), timeEndListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                break;

            case R.id.button_confirm:
                // verify input data
                verifyData();
                break;

            default:
                break;
        }
    }

    public void verifyData(){
        try {
            offlineLogin.verifyProgramme(mEditTextProgramme.getText().toString());
            offlineLogin.verifyGroupNo(mEditTextGroupNo.getText().toString());
            classSchedule.verifySubject(mEditTextSubject.getText().toString());
            classSchedule.verifyTutorlecturer(mEditTextTutorLecturer.getText().toString());
            classSchedule.verifyLocation(mEditTextLocation.getText().toString());
            // insert data
            addData();
        } catch (InvalidInputException e) {
            new BaseActivity().shortToast(getActivity(), e.getInfo());
        }

    }

    public void addData(){
        // set all the related values into account domain
        classSchedule.setFaculty(mSpinnerFaculty.getSelectedItem().toString());
        classSchedule.setClassType(mSpinnerClassType.getSelectedItem().toString());
        classSchedule.setProgramme(mEditTextProgramme.getText().toString());
        classSchedule.setGroupNo(mEditTextGroupNo.getText().toString());
        classSchedule.setSubject(mEditTextSubject.getText().toString());
        classSchedule.setTutorlecturer(mEditTextTutorLecturer.getText().toString());
        classSchedule.setLocation(mEditTextLocation.getText().toString());
        classSchedule.setDay(mSpinnerDate.getSelectedItem().toString());
        classSchedule.setStartTime(mTextViewStartTime.getText().toString());
        classSchedule.setEndTime(mTextViewEndTime.getText().toString());
        classSchedule.setBackendID(String.valueOf(new BaseActivity().getLoginDetail(getActivity()).getLoginId()));

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            new AddClassSchedule(classSchedule).execute();
            BaseActivity.shortToast(getActivity(), "New Class Schedule Created.");
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    public class AddClassSchedule extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        ClassSchedule classSchedule;

        public AddClassSchedule(ClassSchedule classSchedule) {
            this.classSchedule = classSchedule;
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

            data.put("backendID", String.valueOf(this.classSchedule.getBackendID()));
            data.put("faculty", this.classSchedule.getFaculty());
            data.put("classType", this.classSchedule.getClassType());
            data.put("programme", this.classSchedule.getProgramme());
            data.put("groupNo", this.classSchedule.getGroupNo());
            data.put("subject", this.classSchedule.getSubject());
            data.put("tutorLecturer", this.classSchedule.getTutorlecturer());
            data.put("location", this.classSchedule.getLocation());
            data.put("day", this.classSchedule.getDay());
            data.put("startTime", this.classSchedule.getStartTime());
            data.put("endTime", this.classSchedule.getEndTime());

            return requestHandler.sendPostRequest(Add_NEW_CLASS_TIMETABLE_URL, data);
        }
    }
}
