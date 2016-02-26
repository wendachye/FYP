package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract.AccountRecord;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearhAccountFragment extends Fragment implements View.OnClickListener{

    private EditText mEditTextAccountID;
    private Button mButtonSearch;
    private CardView mCardViewEditAccount;
    private TextView mTextViewAccountID;
    private TextView mTextViewName;
    private TextView mTextViewContactNo;
    private TextView mTextViewEmail;
    private TextView mTextViewAuthorization;
    private TextView mTextViewAccountType;
    private TextView mTextViewStatus;

    private static final String SEARCH_ACCOUNT_URL = "http://fypproject.host56.com/Account/search_account.php";
    private static final String KEY_RESPONSE = "Response";
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

        mCardViewEditAccount.setVisibility(View.GONE);

        return view;
    }

    private void setFindviewbyid(View view) {
        mEditTextAccountID = (EditText) view.findViewById(R.id.edit_text_accountID);
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        mButtonSearch.setOnClickListener(this);
        mCardViewEditAccount = (CardView) view.findViewById(R.id.edit_account);
        mCardViewEditAccount.setOnClickListener(this);
        mTextViewAccountID = (TextView) view.findViewById(R.id.text_view_accountID);
        mTextViewName = (TextView) view.findViewById(R.id.text_view_fullname);
        mTextViewContactNo = (TextView) view.findViewById(R.id.text_view_contactNo);
        mTextViewStatus = (TextView) view.findViewById(R.id.text_view_status);
        mTextViewEmail = (TextView) view.findViewById(R.id.text_view_email);
        mTextViewAuthorization = (TextView) view.findViewById(R.id.text_view_authorization);
        mTextViewAccountType = (TextView) view.findViewById(R.id.text_view_accountType);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                new searchAccount(mEditTextAccountID.getText().toString()).execute();
                break;

            case R.id.edit_account:

                break;

            default:
                break;
        }
    }

    // this is get json
    public class searchAccount extends AsyncTask<String, Void, String> {

        String accountID;
        RequestHandler rh = new RequestHandler();

        public searchAccount(String accountID) {
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
            extractJsonData(json);

            switch (account.getResponse()){
                case 1:
                    // class schedule found
                    mCardViewEditAccount.setVisibility(View.VISIBLE);
                    initialValues();
                    break;

                case 2:
                    // class schedule inactive

                    break;

                case 0:
                    // class schedule not found

                    break;

                default:
                    break;
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put("accountID", accountID);
            return rh.sendPostRequest(SEARCH_ACCOUNT_URL, data);
        }
    }

    private void extractJsonData(String json) {

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray(BaseActivity.JSON_ARRAY);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            account.setAccountID(jsonObject.getString(AccountRecord.KEY_ACCOUNT_ID));
            account.setName(jsonObject.getString(AccountRecord.KEY_NAME));
            account.setEmailAddress(jsonObject.getString(AccountRecord.KEY_EMAIL_ADDRESS));
            account.setContactNo(jsonObject.getString(AccountRecord.KEY_CONTACT_NO));
            account.setAuthorization(jsonObject.getString(AccountRecord.KEY_AUTHORIZATION));
            account.setAccountType(jsonObject.getString(AccountRecord.KEY_ACCOUNT_TYPE));
            account.setStatus(jsonObject.getString(AccountRecord.KEY_STATUS));
            account.setResponse(jsonObject.getInt(KEY_RESPONSE));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initialValues() {
        mTextViewAccountID.setText(account.getAccountID());
        mTextViewName.setText(account.getName());
        mTextViewEmail.setText(account.getEmailAddress());
        mTextViewContactNo.setText(account.getContactNo());
        mTextViewAccountType.setText(account.getAccountType());
        mTextViewAuthorization.setText(account.getAuthorization());
        mTextViewStatus.setText(account.getStatus());
    }
}
