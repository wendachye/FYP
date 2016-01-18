package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wenda.tarucnfc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment implements View.OnClickListener {

    Button mButtonPayment, mButtonTopUp, mButtonTransfer;

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
    public void onClick(View v) {

    }
}
