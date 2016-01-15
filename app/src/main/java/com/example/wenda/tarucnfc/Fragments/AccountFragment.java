package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.R;


public class AccountFragment extends Fragment {

    TextView mTextStudentId;
    TextView mTextProgramme;
    TextView mTextFaculty;
    TextView mTextCampus;
    TextView mTextSchoolEmail;
    TextView mTextSessionJoined;
    TextView mTextFullName;
    TextView mTextNRICNO;
    TextView mTextGender;
    TextView mTextEmail;
    TextView mTextContactNo;
    TextView mTextHomeAddress;
    TextView mTextCampusAddress;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mTextStudentId = (TextView) view.findViewById(R.id.text_studentid);
        mTextProgramme = (TextView) view.findViewById(R.id.text_programme);
        mTextFaculty = (TextView) view.findViewById(R.id.text_faculty);
        mTextCampus = (TextView) view.findViewById(R.id.text_campus);
        mTextSchoolEmail = (TextView) view.findViewById(R.id.text_school_email);
        mTextSessionJoined = (TextView) view.findViewById(R.id.text_sessionJoined);
        mTextFullName = (TextView) view.findViewById(R.id.text_fullname);
        mTextNRICNO = (TextView) view.findViewById(R.id.text_nricno);
        mTextGender = (TextView) view.findViewById(R.id.text_gender);
        mTextEmail = (TextView) view.findViewById(R.id.text_email);
        mTextContactNo = (TextView) view.findViewById(R.id.text_contactNo);
        mTextHomeAddress = (TextView) view.findViewById(R.id.text_homeAddress);
        mTextCampusAddress = (TextView) view.findViewById(R.id.text_campusAddress);

        // set database data to data field
        OfflineLogin offlineLogin = new BaseActivity().getLoginDetail(getActivity());
        mTextStudentId.setText(offlineLogin.getAccountID());
        mTextProgramme.setText(offlineLogin.getProgramme());
        mTextFaculty.setText(offlineLogin.getFaculty());
        mTextCampus.setText(offlineLogin.getCampus());
        mTextSchoolEmail.setText(offlineLogin.getSchoolEmail());
        mTextSessionJoined.setText(offlineLogin.getSessionJoined());
        mTextFullName.setText(offlineLogin.getName());
        mTextNRICNO.setText(offlineLogin.getNRICNo());
        mTextGender.setText(offlineLogin.getGender());
        mTextEmail.setText(offlineLogin.getEmailAddress());
        mTextContactNo.setText(offlineLogin.getContactNo());
        mTextHomeAddress.setText(offlineLogin.getHomeAddress());
        mTextCampusAddress.setText(offlineLogin.getCampusAddress());

        return view;
    }

}
