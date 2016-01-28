package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.AdapterClassSchedule;
import com.example.wenda.tarucnfc.Databases.Contracts.ClassScheduleContract.ClassScheduleRecord;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassTimetableFragment extends Fragment {

    private String condition;
    RecyclerView mRecyclerView;

    final static String GET_CLASS_SCHEDULE_URL = "";
    private ClassSchedule classSchedule = new ClassSchedule();
    private JSONArray mJsonArray;
    private ArrayList<ClassSchedule> mListClassSchedule = new ArrayList<>();

    public ClassTimetableFragment() {
        // Required empty public constructor
    }

    public ClassTimetableFragment(String condition) {
        this.condition = condition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_timetable, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        switch (condition) {
            case "Monday":
                new GetJson(String.valueOf(condition)).execute();
                break;

            case "Tuesday":

                break;

            case "Wednesday":

                break;

            case "Thursday":

                break;

            case "Friday":

                break;

            case "Saturday":

                break;

            default:
                break;
        }


        return view;
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String day;
        RequestHandler rh = new RequestHandler();

        public GetJson(String day) {
            this.day = day;
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
            convertJson(json);
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("day", String.valueOf(condition));
            return rh.sendPostRequest(GET_CLASS_SCHEDULE_URL, data);
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

                classSchedule.setFaculty(jsonObject.getString(ClassScheduleRecord.COLUMN_FACULTY));
                classSchedule.setProgramme(jsonObject.getString(ClassScheduleRecord.COLUMN_PROGRAMME));
                classSchedule.setGroup(jsonObject.getString(ClassScheduleRecord.COLUMN_GROUP));
                classSchedule.setSubject(jsonObject.getString(ClassScheduleRecord.COLUMN_SUBJECT));
                classSchedule.setTutorlecturer(jsonObject.getString(ClassScheduleRecord.COLUMN_TUTORLECTURER));
                classSchedule.setLocation(jsonObject.getString(ClassScheduleRecord.COLUMN_LOCATION));
                classSchedule.setDay(jsonObject.getString(ClassScheduleRecord.COLUMN_DAY));
                classSchedule.setStartTime(jsonObject.getString(ClassScheduleRecord.COLUMN_START_TIME));
                classSchedule.setEndTime(jsonObject.getString(ClassScheduleRecord.COLUMN_END_TIME));

                mListClassSchedule.add(classSchedule);
                Log.d("track", mListClassSchedule.size()+ "");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }

        initRecyclerView();
    }

    public void initRecyclerView() {
        AdapterClassSchedule adapterClassSchedule;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterClassSchedule = new AdapterClassSchedule(getActivity(), mListClassSchedule, R.layout.row_class_schedule);
        mRecyclerView.setAdapter(adapterClassSchedule);
    }
}
