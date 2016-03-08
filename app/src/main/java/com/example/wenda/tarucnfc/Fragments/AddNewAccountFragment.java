package com.example.wenda.tarucnfc.Fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.Domains.Login;
import com.example.wenda.tarucnfc.Domains.OfflineLogin;
import com.example.wenda.tarucnfc.InvalidInputException;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;

import java.util.HashMap;


public class AddNewAccountFragment extends Fragment implements View.OnClickListener, TextWatcher{

    private CardView mCradView1;
    private CardView mCardView2;
    private CardView mCardView3;

    private Spinner mSpinnerAccountType;
    private Spinner mSpinnerAccountEndUserAuthorization;
    private Spinner mSpinnerAccountBackEndAuthorization;
    private Spinner mSpinnerGender;

    private LinearLayout mLinearLayoutEndUser;
    private LinearLayout mLinearLayoutBackEnd;
    private LinearLayout mLinearLayoutCampus;

    private EditText mEditTextFullName;
    private EditText mEditTextNRICNo;
    private EditText mEditTextContact;
    private EditText mEditTextEmail;
    private EditText mEditTextHomeAddress;
    private EditText mEditTextCampusAddress;
    private EditText mEditTextAccountID;
    private EditText mEditTextProgramme;
    private EditText mEditTextGroupNo;
    private Spinner mSpinnerFaculty;
    private Spinner mSpinnerCampus;
    private EditText mEditTextSchoolEmail;
    private EditText mEditTextSessionJoined;

    private TextView mTextViewLoginID;
    private TextView mTextViewPassword;

    private Button mButtonConfirm;

    private Account account = new Account();
    private Login login = new Login();
    private OfflineLogin offlineLogin = new OfflineLogin();
    private final static String Add_NEW_ACCOUNT_URL = "http://fypproject.host56.com/Account/add_account.php";

    public AddNewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_account, container, false);

        // setfindviewbyid
        setFindviewbyid(view);

        mSpinnerAccountType.setSelection(0);

        mSpinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (mSpinnerAccountType.getSelectedItemPosition()) {
                    case 0:
                        mCradView1.setVisibility(View.GONE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.GONE);
                        mLinearLayoutEndUser.setVisibility(View.GONE);
                        mLinearLayoutBackEnd.setVisibility(View.GONE);
                        mButtonConfirm.setVisibility(View.GONE);
                        break;

                    case 1:
                        mCradView1.setVisibility(View.GONE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.GONE);
                        mButtonConfirm.setVisibility(View.GONE);
                        mLinearLayoutEndUser.setVisibility(View.VISIBLE);
                        mLinearLayoutBackEnd.setVisibility(View.GONE);
                        mSpinnerAccountEndUserAuthorization.setSelection(0);
                        break;

                    case 2:
                        mCradView1.setVisibility(View.GONE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.GONE);
                        mButtonConfirm.setVisibility(View.GONE);
                        mLinearLayoutEndUser.setVisibility(View.GONE);
                        mLinearLayoutBackEnd.setVisibility(View.VISIBLE);
                        mSpinnerAccountBackEndAuthorization.setSelection(0);
                        break;

                    default:
                        Toast.makeText(getActivity(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerAccountEndUserAuthorization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (mSpinnerAccountEndUserAuthorization.getSelectedItemPosition()) {
                    case 0:
                        mCradView1.setVisibility(View.GONE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.GONE);
                        mButtonConfirm.setVisibility(View.GONE);
                        break;

                    case 1:
                        mEditTextAccountID.setHint("Student ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.VISIBLE);
                        mCardView3.setVisibility(View.VISIBLE);
                        mLinearLayoutCampus.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        mEditTextAccountID.setHint("Lecturer / Tutor ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.VISIBLE);

                        mLinearLayoutCampus.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        mEditTextAccountID.setHint("Lecturer / Tutor ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.VISIBLE);
                        mCardView3.setVisibility(View.VISIBLE);
                        mLinearLayoutCampus.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerAccountBackEndAuthorization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (mSpinnerAccountBackEndAuthorization.getSelectedItemPosition()) {
                    case 0:
                        mCradView1.setVisibility(View.GONE);
                        mCardView2.setVisibility(View.GONE);
                        mCardView3.setVisibility(View.GONE);
                        mButtonConfirm.setVisibility(View.GONE);
                        break;

                    case 1:
                        mEditTextAccountID.setHint("Admin ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView3.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.GONE);
                        mLinearLayoutCampus.setVisibility(View.GONE);
                        break;

                    case 2:
                        mEditTextAccountID.setHint("Student Affair Department ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView3.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.GONE);
                        mLinearLayoutCampus.setVisibility(View.GONE);
                        break;

                    case 3:
                        mEditTextAccountID.setHint("Stall Owner ID");
                        mCradView1.setVisibility(View.VISIBLE);
                        mCardView3.setVisibility(View.VISIBLE);
                        mButtonConfirm.setVisibility(View.VISIBLE);
                        mCardView2.setVisibility(View.GONE);
                        mLinearLayoutCampus.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEditTextAccountID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextViewLoginID.setText(mEditTextAccountID.getText().toString());
            }
        });

        mEditTextNRICNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    account.verifyAccountID(mEditTextAccountID.getText().toString());
                    account.verifyNRICNo(mEditTextNRICNo.getText().toString());

                    String accountString = mEditTextAccountID.getText().toString();
                    String accountSub = accountString.substring(0, 2);
                    String accountSub2 = accountString.substring(5, 10);
                    mTextViewLoginID.setText(accountSub + accountSub2);
                    mTextViewPassword.setText(mEditTextNRICNo.getText().toString());
                } catch (InvalidInputException e) {
                    new BaseActivity().shortToast(getActivity(), e.getInfo());
                }
            }
        });

        return view;
    }

    public void setFindviewbyid(View view) {
        mCradView1 = (CardView) view.findViewById(R.id.cardview1);
        mCardView2 = (CardView) view.findViewById(R.id.cardview2);
        mCardView3 = (CardView) view.findViewById(R.id.cardview3);
        mSpinnerAccountType = (Spinner) view.findViewById(R.id.spinner_account_type);
        mSpinnerAccountEndUserAuthorization = (Spinner) view.findViewById(R.id.spinner_enduser_authorization);
        mSpinnerAccountBackEndAuthorization = (Spinner) view.findViewById(R.id.spinner_backend_authorization);
        mLinearLayoutEndUser = (LinearLayout) view.findViewById(R.id.linear_layout_enduser);
        mLinearLayoutBackEnd = (LinearLayout) view.findViewById(R.id.linear_layout_backend);
        mEditTextFullName = (EditText) view.findViewById(R.id.edit_text_fullname);
        mEditTextNRICNo = (EditText) view.findViewById(R.id.edit_text_nric);
        mEditTextContact = (EditText) view.findViewById(R.id.edit_text_contact);
        mEditTextEmail = (EditText) view.findViewById(R.id.edit_text_email);
        mSpinnerGender = (Spinner) view.findViewById(R.id.spinner_gender);
        mEditTextHomeAddress = (EditText) view.findViewById(R.id.edit_text_homeAddress);
        mEditTextCampusAddress = (EditText) view.findViewById(R.id.edit_text_campusAddress);
        mEditTextAccountID = (EditText) view.findViewById(R.id.edit_text_accountID);
        mEditTextProgramme = (EditText) view.findViewById(R.id.edit_text_programme);
        mEditTextGroupNo = (EditText) view.findViewById(R.id.edit_text_groupNo);
        mSpinnerFaculty = (Spinner) view.findViewById(R.id.spinner_faculty);
        mSpinnerCampus = (Spinner) view.findViewById(R.id.spinner_campus);
        mEditTextSchoolEmail = (EditText) view.findViewById(R.id.edit_text_school_email);
        mEditTextSessionJoined = (EditText) view.findViewById(R.id.edit_text_sessionJoined);
        mLinearLayoutCampus = (LinearLayout) view.findViewById(R.id.linear_layout_campus);
        mButtonConfirm = (Button) view.findViewById(R.id.button_confirm);
        mButtonConfirm.setOnClickListener(this);
        mTextViewLoginID = (TextView) view.findViewById(R.id.text_view_loginID);
        mTextViewPassword = (TextView) view.findViewById(R.id.text_view_password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_confirm:
                // verify input data
                verifyData();
                break;

            default:
                break;
        }
    }

    public void verifyData(){
        try {
            if (mSpinnerAccountType.getSelectedItem().toString().equals("Back End")) {
                account.verifyAccountID(mEditTextAccountID.getText().toString());
                account.verifyName(mEditTextFullName.getText().toString());
                account.verifyNRICNo(mEditTextNRICNo.getText().toString());
                account.verifyContactNo(mEditTextContact.getText().toString());
                account.verifyEmail(mEditTextEmail.getText().toString());
                account.verifyHomeAddress(mEditTextHomeAddress.getText().toString());
                // insert data
                addData();
            } else if (mSpinnerAccountEndUserAuthorization.getSelectedItem().toString().equals("Lecturer / Tutor")){
                account.verifyAccountID(mEditTextAccountID.getText().toString());
                account.verifyName(mEditTextFullName.getText().toString());
                account.verifyNRICNo(mEditTextNRICNo.getText().toString());
                account.verifyContactNo(mEditTextContact.getText().toString());
                account.verifyEmail(mEditTextEmail.getText().toString());
                account.verifyHomeAddress(mEditTextHomeAddress.getText().toString());
                account.verifyCampusAddress(mEditTextCampusAddress.getText().toString());
                // insert data
                addData();
            } else {
                account.verifyAccountID(mEditTextAccountID.getText().toString());
                account.verifyName(mEditTextFullName.getText().toString());
                account.verifyNRICNo(mEditTextNRICNo.getText().toString());
                account.verifyContactNo(mEditTextContact.getText().toString());
                account.verifyEmail(mEditTextEmail.getText().toString());
                account.verifyHomeAddress(mEditTextHomeAddress.getText().toString());
                account.verifyCampusAddress(mEditTextCampusAddress.getText().toString());
                offlineLogin.verifyProgramme(mEditTextProgramme.getText().toString());
                offlineLogin.verifyGroupNo(mEditTextGroupNo.getText().toString());
                offlineLogin.verifySchoolEmail(mEditTextSchoolEmail.getText().toString());
                // insert data
                addData();
            }
        } catch (InvalidInputException e) {
            new BaseActivity().shortToast(getActivity(), e.getInfo());
        }
    }

    public void addData() {
        // set all the related values into account domain
        account.setAccountID(mEditTextAccountID.getText().toString());
        account.setName(mEditTextFullName.getText().toString());
        account.setNRICNo(mEditTextNRICNo.getText().toString());
        account.setContactNo(mEditTextContact.getText().toString());
        account.setEmailAddress(mEditTextEmail.getText().toString());
        account.setGender(mSpinnerGender.getSelectedItem().toString());
        account.setHomeAddress(mEditTextHomeAddress.getText().toString());
        account.setCampusAddress(mEditTextCampusAddress.getText().toString());
        account.setProgramme(mEditTextProgramme.getText().toString());
        account.setGroupNo(mEditTextGroupNo.getText().toString());
        account.setFaculty(mSpinnerFaculty.getSelectedItem().toString());
        account.setCampus(mSpinnerCampus.getSelectedItem().toString());
        account.setSchoolEmail(mEditTextSchoolEmail.getText().toString());
        account.setSessionJoined(mEditTextSessionJoined.getText().toString());
        account.setAccountType(mSpinnerAccountType.getSelectedItem().toString());

        if (mSpinnerAccountType.getSelectedItem().toString().equals("End User")) {
            account.setAuthorization(mSpinnerAccountEndUserAuthorization.getSelectedItem().toString());
        } else {
            account.setAuthorization(mSpinnerAccountBackEndAuthorization.getSelectedItem().toString());
        }

        login.setLoginId(mTextViewLoginID.getText().toString());
        login.setPassword(mTextViewPassword.getText().toString());

        // check network
        if(new BaseActivity().isNetworkAvailable(getActivity()) == true) {
            new AddAccount(account, login).execute();
            clearData();
            BaseActivity.shortToast(getActivity(), "New Account Created.");
        } else {
            BaseActivity.shortToast(getActivity(), "Network not available");
        }
    }

    public void clearData(){
        mSpinnerAccountType.setSelection(0);
        mEditTextAccountID.setText("");
        mEditTextNRICNo.setText("");
        mEditTextFullName.setText("");
        mEditTextContact.setText("");
        mEditTextEmail.setText("");
        mSpinnerGender.setSelection(0);
        mEditTextHomeAddress.setText("");
        mEditTextCampusAddress.setText("");
        mSpinnerFaculty.setSelection(0);
        mEditTextProgramme.setText("");
        mEditTextGroupNo.setText("");
        mSpinnerCampus.setSelection(0);
        mEditTextSchoolEmail.setText("");
        mEditTextSessionJoined.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public class AddAccount extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;
        RequestHandler requestHandler = new RequestHandler();
        Account account;
        Login login;

        public AddAccount(Account account, Login login) {
            this.account = account;
            this.login = login;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UIUtils.getProgressDialog(getActivity(), "ON");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //UIUtils.getProgressDialog(getActivity(), "OFF");
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> data = new HashMap<>();

            data.put("accountID", this.account.getAccountID());
            data.put("name", this.account.getName());
            data.put("NRICNo", this.account.getNRICNo());
            data.put("contactNo", this.account.getContactNo());
            data.put("emailAddress", this.account.getEmailAddress());
            data.put("gender", this.account.getGender());
            data.put("homeAddress", this.account.getHomeAddress());
            data.put("campusAddress", this.account.getCampusAddress());
            data.put("accountType", this.account.getAccountType());
            data.put("programme", this.account.getProgramme());
            data.put("groupNo", this.account.getGroupNo());
            data.put("faculty", this.account.getFaculty());
            data.put("campus", this.account.getCampus());
            data.put("schoolEmail", this.account.getSchoolEmail());
            data.put("sessionJoined", this.account.getSessionJoined());
            data.put("loginID", this.login.getLoginId());
            data.put("password", this.login.getPassword());
            data.put("authorization", this.account.getAuthorization());

            return requestHandler.sendPostRequest(Add_NEW_ACCOUNT_URL, data);
        }
    }
}
