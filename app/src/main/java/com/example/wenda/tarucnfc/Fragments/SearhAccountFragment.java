package com.example.wenda.tarucnfc.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.R;

public class SearhAccountFragment extends Fragment implements View.OnClickListener{

    private EditText mEditTextAccountID;
    private Button mButtonSearch;

    private Account account = new Account();


    public SearhAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searh_account, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        return view;
    }

    private void setFindviewbyid(View view) {
        mEditTextAccountID = (EditText) view.findViewById(R.id.edit_text_accountID);
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:

                break;

            //case R.id.edit_bus_schedule:



            default:
                break;
        }
    }
}
