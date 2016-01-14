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

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    TextView mTextStudentId;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mTextStudentId = (TextView) view.findViewById(R.id.edit_text_studentid);

        OfflineLogin offlineLogin = new BaseActivity().getLoginDetail(getActivity());
        mTextStudentId.setText(offlineLogin.getAccountID());
        return view;
    }

}
