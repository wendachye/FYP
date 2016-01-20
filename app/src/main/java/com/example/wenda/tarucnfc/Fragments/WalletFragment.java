package com.example.wenda.tarucnfc.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wenda.tarucnfc.Activitys.PinEntryActivity;
import com.example.wenda.tarucnfc.R;


public class WalletFragment extends Fragment implements View.OnClickListener {

    Button mButtonPayment, mButtonTopUp, mButtonTransfer;

    public static final String KEY_SELECTED = "selected";

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        initialValues(view);

        return view;
    }

    private void initialValues(View view) {
        mButtonPayment = (Button) view.findViewById(R.id.payment);
        mButtonPayment.setOnClickListener(this);

        mButtonTopUp = (Button) view.findViewById(R.id.topup);
        mButtonTopUp.setOnClickListener(this);

        mButtonTransfer = (Button) view.findViewById(R.id.transfer);
        mButtonTransfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payment:
                Intent intent = new Intent(getActivity(), PinEntryActivity.class);
                intent.putExtra(KEY_SELECTED, "payment");
                startActivity(intent);
                break;

            case R.id.topup:
                Intent intent2 = new Intent(getActivity(), PinEntryActivity.class);
                intent2.putExtra(KEY_SELECTED, "topUp");
                startActivity(intent2);
                break;

            case R.id.transfer:
                Intent intent3 = new Intent(getActivity(), PinEntryActivity.class);
                intent3.putExtra(KEY_SELECTED, "transfer");
                startActivity(intent3);
                break;

            default:
                break;
        }
    }
}
