package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.AdapterClassSchedule;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;
import com.example.wenda.tarucnfc.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassTimetableFragment extends Fragment {

    private String condition;
    RecyclerView mRecyclerView;
    TextView mTextViewTimetableTitle;

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

        mTextViewTimetableTitle = (TextView) view.findViewById(R.id.timetable_title);

        switch (condition) {
            case "Monday":
                mTextViewTimetableTitle.setText("Monday Timetable");
                break;

            case "Tuesday":
                mTextViewTimetableTitle.setText("Tuesday Timetable");
                break;

            case "Wednesday":
                mTextViewTimetableTitle.setText("Wednesday Timetable");
                break;

            case "Thursday":
                mTextViewTimetableTitle.setText("Thursday Timetable");
                break;

            case "Friday":
                mTextViewTimetableTitle.setText("Friday Timetable");
                break;

            case "Saturday":
                mTextViewTimetableTitle.setText("Saturday Timetable");
                break;

            default:
                break;
        }

        ClassSchedule classSchedule = new ClassSchedule();
        classSchedule.setFaculty("1");
        classSchedule.setProgramme("2");
        classSchedule.setGroup("3");
        classSchedule.setSubject("4");
        classSchedule.setTutorlecturer("5");
        classSchedule.setLocation("6");
        classSchedule.setDate("7");
        classSchedule.setTime("8");

        mListClassSchedule.add(classSchedule);

        initRecyclerView();

        return view;
    }

    public void initRecyclerView() {
        AdapterClassSchedule adapterClassSchedule;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterClassSchedule = new AdapterClassSchedule(getContext(), mListClassSchedule, R.layout.row_class_schedule);
        mRecyclerView.setAdapter(adapterClassSchedule);
    }

}
