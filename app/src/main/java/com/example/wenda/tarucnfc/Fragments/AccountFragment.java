package com.example.wenda.tarucnfc.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Databases.Contracts.AccountContract.AccountRecord;
import com.example.wenda.tarucnfc.Domains.Account;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class AccountFragment extends Fragment {

    private final static String GET_JSON_URL = "http://fypproject.host56.com/Account/get_account_view.php";
    private TextView mTextViewStudentId;
    private TextView mTextViewProgramme;
    private TextView mTextViewFaculty;
    private TextView mTextViewCampus;
    private TextView mTextViewSchoolEmail;
    private TextView mTextViewSessionJoined;
    private TextView mTextViewFullName;
    private TextView mTextViewNRICNO;
    private TextView mTextViewGender;
    private TextView mTextViewEmail;
    private TextView mTextViewContactNo;
    private TextView mTextViewHomeAddress;
    private TextView mTextViewCampusAddress;
    private TextView mTextViewGroupNo;
    private ImageView mImage_profile;
    private LinearLayout mCampusAddress;
    private CardView mStudentInfo;
    private Account account = new Account();
    private String mAccountID;
    private SwipeRefreshLayout mSwipeContainer;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAccountID = new BaseActivity().getLoginDetail(getActivity()).getAccountID();

        // set findviewbyid
        setfindviewbyid(view);

        if (new BaseActivity().isNetworkAvailable(getActivity())) {
            new GetJson(String.valueOf(mAccountID)).execute();
        } else {
            new BaseActivity().shortToast(getActivity(), "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (new BaseActivity().isNetworkAvailable(getActivity())) {
                    new GetJson(String.valueOf(mAccountID)).execute();
                } else {
                    new BaseActivity().shortToast(getActivity(), "Network not available, couldn't refresh.");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });


        return view;
    }

    public void setfindviewbyid(View view) {
        mCampusAddress = (LinearLayout) view.findViewById(R.id.campus_address);
        mStudentInfo = (CardView) view.findViewById(R.id.student_info);
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mTextViewStudentId = (TextView) view.findViewById(R.id.text_studentid);
        mTextViewProgramme = (TextView) view.findViewById(R.id.text_programme);
        mTextViewFaculty = (TextView) view.findViewById(R.id.text_faculty);
        mTextViewCampus = (TextView) view.findViewById(R.id.text_campus);
        mTextViewSchoolEmail = (TextView) view.findViewById(R.id.text_school_email);
        mTextViewSessionJoined = (TextView) view.findViewById(R.id.text_sessionJoined);
        mTextViewFullName = (TextView) view.findViewById(R.id.text_fullname);
        mTextViewNRICNO = (TextView) view.findViewById(R.id.text_nricno);
        mTextViewGender = (TextView) view.findViewById(R.id.text_gender);
        mTextViewEmail = (TextView) view.findViewById(R.id.text_email);
        mTextViewContactNo = (TextView) view.findViewById(R.id.text_contactNo);
        mTextViewHomeAddress = (TextView) view.findViewById(R.id.text_homeAddress);
        mTextViewCampusAddress = (TextView) view.findViewById(R.id.text_campusAddress);
        mTextViewGroupNo = (TextView) view.findViewById(R.id.text_groupNo);
        mImage_profile = (ImageView) view.findViewById(R.id.image_profile);
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

            account.setAccountID(jsonObject.getString(AccountRecord.KEY_ACCOUNT_ID));
            account.setFaculty(jsonObject.getString(AccountRecord.KEY_FACULTY));
            account.setProgramme(jsonObject.getString(AccountRecord.KEY_PROGRAMME));
            account.setGroupNo(jsonObject.getString(AccountRecord.KEY_GROUPNO));
            account.setCampus(jsonObject.getString(AccountRecord.KEY_CAMPUS));
            account.setSchoolEmail(jsonObject.getString(AccountRecord.KEY_SCHOOL_EMAIL));
            account.setSessionJoined(jsonObject.getString(AccountRecord.KEY_SESSION_JOINED));
            account.setName(jsonObject.getString(AccountRecord.KEY_NAME));
            account.setNRICNo(jsonObject.getString(AccountRecord.KEY_NRIC_NO));
            account.setContactNo(jsonObject.getString(AccountRecord.KEY_CONTACT_NO));
            account.setEmailAddress(jsonObject.getString(AccountRecord.KEY_EMAIL_ADDRESS));
            account.setGender(jsonObject.getString(AccountRecord.KEY_GENDER));
            account.setHomeAddress(jsonObject.getString(AccountRecord.KEY_HOME_ADDRESS));
            account.setCampusAddress(jsonObject.getString(AccountRecord.KEY_CAMPUS_ADDRESS));
            account.setAccountType(jsonObject.getString(AccountRecord.KEY_ACCOUNT_TYPE));
            account.setAccountBalance(jsonObject.getString(AccountRecord.KEY_ACCOUNT_BALANCE));
            account.setPINcode(jsonObject.getString(AccountRecord.KEY_PIN_CODE));
            account.setStatus(jsonObject.getString(AccountRecord.KEY_STATUS));
            account.setProfilePicturePath(jsonObject.getString(AccountRecord.KEY_PROFILE_PICTURE_PATH));
            account.setGroupNo(jsonObject.getString(AccountRecord.KEY_GROUPNO));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("track", "error");
        }

        initialValues();
    }

    public void initialValues() {

        if (account.getAccountType().equals("Back End")) {
            mStudentInfo.setVisibility(View.GONE);
            mCampusAddress.setVisibility(View.GONE);
        } else {
            mCampusAddress.setVisibility(View.VISIBLE);
            mTextViewCampusAddress.setText(account.getCampusAddress());
        }

        mTextViewStudentId.setText(account.getAccountID());
        mTextViewProgramme.setText(account.getProgramme());
        mTextViewFaculty.setText(account.getFaculty());
        mTextViewCampus.setText(account.getCampus());
        mTextViewSchoolEmail.setText(account.getSchoolEmail());
        mTextViewSessionJoined.setText(account.getSessionJoined());
        mTextViewFullName.setText(account.getName());
        mTextViewNRICNO.setText(account.getNRICNo());
        mTextViewGender.setText(account.getGender());
        mTextViewEmail.setText(account.getEmailAddress());
        mTextViewContactNo.setText(account.getContactNo());
        mTextViewHomeAddress.setText(account.getHomeAddress());
        mTextViewGroupNo.setText(account.getGroupNo());
        ImageLoader.getInstance().displayImage(account.getProfilePicturePath(), mImage_profile, new BaseActivity().options);
    }

}
