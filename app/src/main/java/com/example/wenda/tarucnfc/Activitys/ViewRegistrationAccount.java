package com.example.wenda.tarucnfc.Activitys;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wenda.tarucnfc.Adapter.AdapterRegistration;
import com.example.wenda.tarucnfc.Adapter.AdapterTransaction;
import com.example.wenda.tarucnfc.Databases.Contracts.TransactionContract;
import com.example.wenda.tarucnfc.Domains.Registration;
import com.example.wenda.tarucnfc.Domains.Transaction;
import com.example.wenda.tarucnfc.R;
import com.example.wenda.tarucnfc.RequestHandler;
import com.example.wenda.tarucnfc.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewRegistrationAccount extends BaseActivity {

    private AdapterRegistration adapterRegistration;
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayoutNoRecord;
    private ArrayList<Registration> mListRegistration = new ArrayList<>();
    private static final String GET_REGISTRATION_URL = "http://fypproject.host56.com/Login/get_registration.php";
    private JSONArray mJsonArray;
    private SwipeRefreshLayout mSwipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_registration_account);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutNoRecord = (LinearLayout) findViewById(R.id.layout_no_record);

        // set title to center
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_registration);

        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        if (isNetworkAvailable(this)) {
            mListRegistration.clear();
            new GetJson().execute();
        } else {
            shortToast(this, "Network not available.");
        }

        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable(ViewRegistrationAccount.this)) {
                    mListRegistration.clear();
                    new GetJson().execute();
                } else {
                    shortToast(ViewRegistrationAccount.this, "Network not available, couldn't refresh.");
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });
    }

    // this one is get json
    public class GetJson extends AsyncTask<String, Void, String> {
        //String accountID;
        RequestHandler rh = new RequestHandler();

        public GetJson() {
            //this.accountID = accountID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UIUtils.getProgressDialog(ViewRegistrationAccount.this, "ON");
        }


        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            UIUtils.getProgressDialog(ViewRegistrationAccount.this, "OFF");
            mSwipeContainer.setRefreshing(false);
            convertJson(json);
            extractJsonData();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> data = new HashMap<>();
            data.put(KEY_ACCOUNT_ID, "");
            return rh.sendPostRequest(GET_REGISTRATION_URL, data);
        }
    }

    // parse JSON data into JSON array
    private void convertJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            mJsonArray = jsonObject.getJSONArray(BaseActivity.JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractJsonData() {

        for (int i = 0; i < mJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = mJsonArray.getJSONObject(i);
                Registration registration = new Registration();
                registration.setRegistrationID(jsonObject.getString("registrationID"));
                registration.setName(jsonObject.getString("name"));
                registration.setNric(jsonObject.getString("nric"));
                registration.setContact(jsonObject.getString("contact"));
                registration.setEmail(jsonObject.getString("email"));

                mListRegistration.add(registration);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("track", "error");
            }
        }
        if (mListRegistration.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearLayoutNoRecord.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterRegistration = new AdapterRegistration(this, mListRegistration, R.layout.row_registration_account);
            mRecyclerView.setAdapter(adapterRegistration);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mLinearLayoutNoRecord.setVisibility(View.VISIBLE);
        }
    }
}
