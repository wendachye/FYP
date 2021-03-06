package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Adapter.AdapterClassSchedule;
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
    private String mFaculty;
    private String mProgramme;
    private String mGroupNo;
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayoutNoRecord;
    private AdapterClassSchedule adapterClassSchedule;
    private SwipeRefreshLayout mSwipeContainer;

    final static String GET_CLASS_SCHEDULE_URL = "http://fypproject.host56.com/ClassSchedule/get_class_schedule_view.php";
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

        mFaculty = new BaseActivity().getLoginDetail(getActivity()).getFaculty();
        mProgramme = new BaseActivity().getLoginDetail(getActivity()).getProgramme();
        mGroupNo = new BaseActivity().getLoginDetail(getActivity()).getGroupNo();


        mLinearLayoutNoRecord = (LinearLayout) view.findViewById(R.id.layout_no_record);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            mListClassSchedule.clear();
            new GetJson(mFaculty, mProgramme, mGroupNo, condition).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    mListClassSchedule.clear();
                    new GetJson(mFaculty, mProgramme, mGroupNo, condition).execute();
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

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String faculty, programme, groupNo, day;
        RequestHandler rh = new RequestHandler();

        public GetJson(String faculty, String programme, String groupNo, String day) {
            this.faculty = faculty;
            this.programme = programme;
            this.groupNo = groupNo;
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
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("faculty", mFaculty);
            data.put("programme", mProgramme);
            data.put("groupNo", mGroupNo);
            data.put("day", condition);
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

    private void extractJsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                ClassSchedule classSchedule = new ClassSchedule();
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
                classSchedule.setClassType(jsonObject.getString(ClassScheduleRecord.COLUMN_CLASS_TYPE));

                if (classSchedule.getStatus().toString().equals("Active")) {
                    mListClassSchedule.add(classSchedule);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        Log.d("track", "dd" +mListClassSchedule.size());
        if (mListClassSchedule.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapterClassSchedule = new AdapterClassSchedule(getActivity(), mListClassSchedule, R.layout.row_class_schedule);
            mRecyclerView.setAdapter(adapterClassSchedule);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }
}
