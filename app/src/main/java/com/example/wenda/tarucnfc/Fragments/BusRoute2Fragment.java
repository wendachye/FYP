package com.example.wenda.tarucnfc.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;

public class BusRoute2Fragment extends Fragment implements View.OnClickListener {

    Button mButtonBusRoute;
    TextView mTextViewDate, mTextViewDate2, mTextViewDate3;
    TextView mTextViewDeparture, mTextViewDeparture2, mTextViewDeparture3;
    TextView mTextViewDestination, mTextViewDestination2, mTextViewDestination3;
    TextView mTextViewTime, mTextViewTime2, mTextViewTime3;
    CardView mCardView1, mCardView2, mCardView3;


    public BusRoute2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_route2, container, false);

        // set findviewbyid
        setFindviewbyid(view);

        return view;
    }

    public void setFindviewbyid(View view) {
        mButtonBusRoute = (Button) view.findViewById(R.id.button_bus_route);

        mTextViewDate = (TextView) view.findViewById(R.id.text_route_date);
        mTextViewDate2 = (TextView) view.findViewById(R.id.text_route_date2);
        mTextViewDate3 = (TextView) view.findViewById(R.id.text_route_date3);

        mTextViewDeparture = (TextView) view.findViewById(R.id.text_departure);
        mTextViewDeparture2 = (TextView) view.findViewById(R.id.text_departure2);
        mTextViewDeparture3 = (TextView) view.findViewById(R.id.text_departure3);

        mTextViewDestination = (TextView) view.findViewById(R.id.text_destination);
        mTextViewDestination2 = (TextView) view.findViewById(R.id.text_destination2);
        mTextViewDestination3 = (TextView) view.findViewById(R.id.text_destination3);

        mTextViewTime = (TextView) view.findViewById(R.id.text_route_time);
        mTextViewTime2 = (TextView) view.findViewById(R.id.text_route_time2);
        mTextViewTime3 = (TextView) view.findViewById(R.id.text_route_time3);

        mCardView1 = (CardView) view.findViewById(R.id.cardview1);
        mCardView1.setOnClickListener(this);
        mCardView2 = (CardView) view.findViewById(R.id.cardview2);
        mCardView2.setOnClickListener(this);
        mCardView3 = (CardView) view.findViewById(R.id.cardview3);
        mCardView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_bus_route:
                Dialog settingsDialog = new Dialog(getContext());
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.route_wangsamaju
                        , null));
                settingsDialog.show();
                break;

            default:
                break;
        }
    }

}
