package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.ClassScheduleContract.ClassScheduleRecord;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchClassScheduleFragment extends Fragment implements View.OnClickListener{

    private String faculty, programme, groupNo, subject, tutorLecturer, classScheduleID;
    private Button mButtonSearch;
    private Spinner mSpinnerFaculty;
    private EditText mEditTextProgramme;
    private EditText mEditTextGroupNo;
    private EditText mEditTextSubject;
    private EditText mEditTextTutorLecturer;
    private TextView mTextViewSubject;
    private TextView mTextViewTutorLecturer;
    private TextView mTextViewLocation;
    private TextView mTextViewDate;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private CardView mCardViewEditClassSchedule;

    private static final String SEARCH_CLASS_SCHEDULE_URL = "http://fypproject.host56.com/ClassSchedule/search_class_schedule.php";
    private static final String KEY_RESPONSE = "Response";
    private ClassSchedule classSchedule = new ClassSchedule();

    public SearchClassScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_class_schedule, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        mCardViewEditClassSchedule.setVisibility(View.GONE);

        return view;
    }

    private void setFindviewbyid(View view) {
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mSpinnerFaculty = (Spinner) view.findViewById(R.id.spinner_faculty);
        mEditTextProgramme = (EditText) view.findViewById(R.id.editText_programme);
        mEditTextGroupNo = (EditText) view.findViewById(R.id.editText_groupNo);
        mEditTextSubject = (EditText) view.findViewById(R.id.editText_subject);
        mEditTextTutorLecturer = (EditText) view.findViewById(R.id.editText_tutorlecturer);
        mTextViewSubject = (TextView) view.findViewById(R.id.text_view_subject);
        mTextViewTutorLecturer = (TextView) view.findViewById(R.id.text_view_tutorlecturer);
        mTextViewLocation = (TextView) view.findViewById(R.id.text_view_location);
        mTextViewDate = (TextView) view.findViewById(R.id.text_view_date);
        mTextViewStartTime = (TextView) view.findViewById(R.id.text_view_startTime);
        mTextViewEndTime = (TextView) view.findViewById(R.id.text_view_endTime);
        mCardViewEditClassSchedule = (CardView) view.findViewById(R.id.edit_class_schedule);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                faculty = mSpinnerFaculty.getSelectedItem().toString();
                programme = mEditTextProgramme.getText().toString();
                groupNo = mEditTextGroupNo.getText().toString();
                subject = mEditTextSubject.getText().toString();
                tutorLecturer = mEditTextTutorLecturer.getText().toString();
                new searchClassSchedule(faculty, programme, groupNo, subject, tutorLecturer).execute();
                break;

            default:
                break;
        }
    }

    // this is get json
    public class searchClassSchedule extends AsyncTask<String, Void, String> {

        String faculty, programme, groupNo, subject, tutorLecturer;
        RequestHandler rh = new RequestHandler();

        public searchClassSchedule(String faculty, String programme, String groupNo, String subject, String tutorLecturer) {
            this.faculty = faculty;
            this.programme = programme;
            this.groupNo = groupNo;
            this.subject = subject;
            this.tutorLecturer = tutorLecturer;
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

            switch (classSchedule.getResponse()){
                case 1:
                    // bus route found
                    mCardViewEditClassSchedule.setVisibility(View.VISIBLE);
                    initialValues();
                    break;

                case 2:
                    // bus route inactive

                    break;

                case 0:
                    // bus route not found

                    break;

                default:
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();

            data.put("faculty", faculty);
            data.put("programme", programme);
            data.put("groupNo", groupNo);
            data.put("subject", subject);
            data.put("tutorLecturer", tutorLecturer);

            return rh.sendPostRequest(SEARCH_CLASS_SCHEDULE_URL, data);
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
            classSchedule.setResponse(jsonObject.getInt(KEY_RESPONSE));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialValues() {
        classScheduleID = classSchedule.getClassScheduleID();
        mTextViewSubject.setText(classSchedule.getSubject());
        mTextViewTutorLecturer.setText(classSchedule.getTutorlecturer());
        mTextViewLocation.setText(classSchedule.getLocation());
        mTextViewDate.setText(classSchedule.getDay());
        mTextViewStartTime.setText(classSchedule.getStartTime());
        mTextViewEndTime.setText(classSchedule.getEndTime());
    }
}
