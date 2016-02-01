package com.example.wenda.tarucnfc.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Activitys.MainActivity;
import com.example.wenda.tarucnfc.Activitys.PinEntryActivity;
import com.example.wenda.tarucnfc.Activitys.TransactionActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class WalletFragment extends Fragment implements View.OnClickListener {

    Button mButtonHistory, mButtonTopUp, mButtonTransfer;
    TextView mTextViewAccountBalance;
    SwipeRefreshLayout mSwipeContainer;
    private Account account = new Account();
    private String mAccountID;
    public static final String KEY_SELECTED = "selected";
    private final static String GET_JSON_URL = "http://tarucandroid.comxa.com/Login/get_account_view.php";

    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        mAccountID = new BaseActivity().getLoginDetail(getActivity()).getAccountID();

        // setFinviewbyid
        setFinviewbyid(view);

        new GetJson(String.valueOf(mAccountID)).execute();

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetJson(String.valueOf(mAccountID)).execute();
            }
        });


        return view;
    }

    private void setFinviewbyid(View view) {
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mTextViewAccountBalance = (TextView) view.findViewById(R.id.account_balance);
        mButtonHistory = (Button) view.findViewById(R.id.history);
        mButtonHistory.setOnClickListener(this);
        mButtonTopUp = (Button) view.findViewById(R.id.topup);
        mButtonTopUp.setOnClickListener(this);
        mButtonTransfer = (Button) view.findViewById(R.id.transfer);
        mButtonTransfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topup:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                Intent intent2 = new Intent(getActivity(), PinEntryActivity.class);
                intent2.putExtra(KEY_SELECTED, "topUp");
                startActivity(intent2);
                MainActivity.main.finish();
                break;

            case R.id.transfer:
                Intent intent3 = new Intent(getActivity(), PinEntryActivity.class);
                intent3.putExtra(KEY_SELECTED, "transfer");
                startActivity(intent3);
                MainActivity.main.finish();
                break;

            case R.id.history:
                Intent intent = new Intent(getActivity(), TransactionActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson(String accountID) {
            this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(getActivity(), "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(getActivity(), "OFF");
            mSwipeContainer.setRefreshing(false);
            extractJsonData(json);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", String.valueOf(mAccountID));
            return rh.sendPostRequest(GET_JSON_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setAccountID(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
            Log.d("track", "get id *" + jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_ID));
            account.setAccountBalance(jsonObject.getString(AccountContract.AccountRecord.KEY_ACCOUNT_BALANCE));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {
        mTextViewAccountBalance.setText(account.getAccountBalance());
    }
}
