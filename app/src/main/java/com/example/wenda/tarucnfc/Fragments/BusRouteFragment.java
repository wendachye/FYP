package com.example.wenda.tarucnfc.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wenda.tarucnfc.R;

public class BusRouteFragment extends Fragment implements View.OnClickListener {

    private String condition;
    Spinner mSpinnerDate;
    Button mButtonRoute;
    TextView mTextViewRouteTitle;
    TextView mTextViewDeparture;
    TextView mTextViewDestination;
    TextView mTextViewTime;
    TextView mTextViewDeparture2;
    TextView mTextViewDestination2;
    TextView mTextViewTime2;
    CardView mCardView1;
    CardView mCardView2;

    public BusRouteFragment() {
        // Required empty public constructor
    }

    public BusRouteFragment(String condition) {
        this.condition = condition;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wangsamaju, container, false);

        // set spinner text size and color
        mSpinnerDate = (Spinner) view.findViewById(R.id.spinner_date);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.busSchedule_arrays, R.layout.spinner_bus);
        dateAdapter.setDropDownViewResource(R.layout.spinner_bus);
        mSpinnerDate.setAdapter(dateAdapter);

        mCardView1 = (CardView) view.findViewById(R.id.cardview1);
        mCardView2 = (CardView) view.findViewById(R.id.cardview2);

        mTextViewRouteTitle = (TextView) view.findViewById(R.id.route_title);
        mButtonRoute = (Button) view.findViewById(R.id.route_wangsamaju);
        mButtonRoute.setOnClickListener(this);
        mTextViewDeparture = (TextView) view.findViewById(R.id.text_departure);
        mTextViewDestination = (TextView) view.findViewById(R.id.text_destination);
        mTextViewTime = (TextView) view.findViewById(R.id.text_time);
        mTextViewDeparture2 = (TextView) view.findViewById(R.id.text_departure2);
        mTextViewDestination2 = (TextView) view.findViewById(R.id.text_destination2);
        mTextViewTime2 = (TextView) view.findViewById(R.id.text_time2);


        switch (condition) {

            case "Wangsa Maju":
                mTextViewRouteTitle.setText("Wangsa Maju Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                mTextViewDeparture.setText("");
                                mTextViewDestination.setText("");
                                mTextViewTime.setText("");
                                mTextViewDeparture2.setText("");
                                mTextViewDestination2.setText("");
                                mTextViewTime2.setText("");
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                mTextViewDeparture.setText("Bus stop 1");
                                mTextViewDestination.setText("Wangsa Maju");
                                mTextViewTime.setText("7:30 8:00");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case  "Genting Klang":
                mTextViewRouteTitle.setText("Genting Klang Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                mTextViewDeparture.setText("");
                                mTextViewDestination.setText("");
                                mTextViewTime.setText("");
                                mTextViewDeparture2.setText("");
                                mTextViewDestination2.setText("");
                                mTextViewTime2.setText("");
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                mTextViewDeparture.setText("Bus stop 2 & 3");
                                mTextViewDestination.setText("Genting Klang");
                                mTextViewTime.setText("7:30 8:00");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "PV10/12/13/15/16":
                mTextViewRouteTitle.setText("PV10/12/13/15/16 Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                mTextViewDeparture.setText("");
                                mTextViewDestination.setText("");
                                mTextViewTime.setText("");
                                mTextViewDeparture2.setText("");
                                mTextViewDestination2.setText("");
                                mTextViewTime2.setText("");
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                mTextViewDeparture.setText("Bus stop 4 & 5");
                                mTextViewDestination.setText("PV10/12/13/15/16");
                                mTextViewTime.setText("7:30 8:00");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Melati Utama":
                mTextViewRouteTitle.setText("Melati Utama Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                mTextViewDeparture.setText("");
                                mTextViewDestination.setText("");
                                mTextViewTime.setText("");
                                mTextViewDeparture2.setText("");
                                mTextViewDestination2.setText("");
                                mTextViewTime2.setText("");
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                mTextViewDeparture.setText("Bus stop 6");
                                mTextViewDestination.setText("Melati Utama");
                                mTextViewTime.setText("7:30 8:00");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Sri Rampai":
                mTextViewRouteTitle.setText("Sri Rampai Bus Schedule");
                mSpinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mCardView1.setVisibility(View.INVISIBLE);
                                mCardView2.setVisibility(View.INVISIBLE);
                                mTextViewDeparture.setText("");
                                mTextViewDestination.setText("");
                                mTextViewTime.setText("");
                                mTextViewDeparture2.setText("");
                                mTextViewDestination2.setText("");
                                mTextViewTime2.setText("");
                                break;

                            case 1:
                                mCardView1.setVisibility(View.VISIBLE);
                                mCardView2.setVisibility(View.VISIBLE);
                                mTextViewDeparture.setText("Bus stop 1");
                                mTextViewDestination.setText("Sri Rampai");
                                mTextViewTime.setText("7:30 8:00");
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            default:
                break;
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.route_wangsamaju:
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
