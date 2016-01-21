package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.R;


public class DashboardFragment extends Fragment implements View.OnClickListener {

    Button mButton;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mButton = (Button) view.findViewById(R.id.customview_button);
        mButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customview_button:
                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(getContext())
                        .customView(R.layout.custom_view_pincode, wrapInScrollView)
                        .show();
                break;
            default:
                break;
        }
    }
}